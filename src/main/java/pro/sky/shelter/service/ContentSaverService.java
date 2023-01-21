package pro.sky.shelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import pro.sky.shelter.core.entity.AnimalPhotoEntity;
import pro.sky.shelter.core.exception.PhotoNotFoundException;
import pro.sky.shelter.core.repository.AnimalPhotoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * Класс для работы с фотографиями - получение, сохранение файла и данных в БД, выгрузка.
 *
 * @autor Shikunov Andrey
 */
@Service
public class ContentSaverService {
    private final Logger logger = LoggerFactory.getLogger(ContentSaverService.class);
    @Value("${path.to.materials.folder}")
    private String materialsDir;
    private final TelegramBot telegramBot;
    private final AnimalPhotoRepository animalPhotoRepository;

    public ContentSaverService(TelegramBot telegramBot, AnimalPhotoRepository animalPhotoRepository) {
        this.telegramBot = telegramBot;
        this.animalPhotoRepository = animalPhotoRepository;
    }

    /**
     * Метод загрузки фотографии в БД
     */
    public void uploadPhoto(Update update) {
        logger.info("Вызов метода загрузки фото из Update");
        Long idChat = update.message().chat().id();
        PhotoSize[] photoSizes = update.message().photo();
        int maxPhotoIndex = photoSizes.length - 1;
        try {
            GetFileResponse getFileResponse = telegramBot.execute(new GetFile(photoSizes[maxPhotoIndex].fileId()));
            byte[] data = telegramBot.getFileContent(getFileResponse.file());
            String filePath = getFileResponse.file().filePath();
            String fileExtension = filePath.substring(filePath.lastIndexOf('.'));

            Path path = getAndCreatePath(idChat, fileExtension);
            if (path == null) {
                logger.error("ChatId={}; метод загрузки фото не смог найти или создать папку", idChat);
                return;
            }
            Files.write(path, data);

            AnimalPhotoEntity animalPhotoEntity = new AnimalPhotoEntity();
            animalPhotoEntity.setData(data);
            animalPhotoEntity.setFileSize(photoSizes[maxPhotoIndex].fileSize());
            animalPhotoEntity.setMediaType(Files.probeContentType(path));
            animalPhotoEntity.setFilePath(path.toString());
            animalPhotoRepository.save(animalPhotoEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод получения либо создания пути сохранения файла.
     *
     * @param idChat     - используется для создания директорий пользователей
     * @param fileFormat - расширение файла
     * @return Path к месту сохранения файла
     */
    private Path getAndCreatePath(Long idChat, String fileFormat) throws IOException {
        StringBuilder pathFolder = new StringBuilder(materialsDir + "/");
        if (checkOrCreateFolder(pathFolder.toString())) {
            pathFolder.append(idChat).append("/");
            if (checkOrCreateFolder(pathFolder.toString())) {
                long count = Files.find(
                        Paths.get(pathFolder.toString()), 1,
                        (path, attributes) -> attributes.isDirectory()
                ).count();
                pathFolder.append("report-").append(count).append("/");
                if (checkOrCreateFolder(pathFolder.toString())) {
                    LocalDateTime ldt = LocalDateTime.now();
                    String fileName =
                            ldt.getYear() + "." + ldt.getMonthValue() + "." + ldt.getDayOfMonth() + "_"
                                    + ldt.getHour() + "." + ldt.getMinute()
                                    + "." + fileFormat;
                    return Path.of(pathFolder + fileName);
                }
            }
        }
        return null;
    }

    /**
     * Метод проверяет есть ли каталог, если нет - создает его.
     *
     * @return True после прохождения
     */
    private boolean checkOrCreateFolder(String path) {
        java.io.File folder = new java.io.File(path);
        if (!folder.exists()) {
            return folder.mkdir();
        }
        return true;
    }

    /**
     * Метод получения фотографии из БД
     *
     * @param id - id фотографии
     * @return возвращает Pair<String, byte[]>
     */
    public Pair<String, byte[]> getPhoto(long id) {
        logger.info("Запущен метод чтения фото из БД");
        AnimalPhotoEntity animalPhotoEntity = animalPhotoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Не найдено фото с id = {}", id);
                    return new PhotoNotFoundException(id);
                });
        return Pair.of(animalPhotoEntity.getMediaType(), animalPhotoEntity.getData());
    }
}
