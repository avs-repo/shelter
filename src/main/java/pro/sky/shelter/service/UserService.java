package pro.sky.shelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.shelter.core.dto.DialogDto;
import pro.sky.shelter.core.entity.AnimalEntity;
import pro.sky.shelter.exception.AnimalNotFoundException;
import pro.sky.shelter.exception.DateNotFoundException;
import pro.sky.shelter.exception.UserNotFoundException;
import pro.sky.shelter.core.record.RecordMapper;
import pro.sky.shelter.core.record.ReportRecord;
import pro.sky.shelter.core.record.UserRecord;
import pro.sky.shelter.core.entity.UserEntity;
import pro.sky.shelter.core.repository.AnimalRepository;
import pro.sky.shelter.core.repository.UserRepository;
import pro.sky.shelter.exception.VolunteerNotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final RecordMapper recordMapper;
    private final TelegramBot telegramBot;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, AnimalRepository animalRepository, RecordMapper recordMapper, TelegramBot telegramBot) {
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
        this.recordMapper = recordMapper;
        this.telegramBot = telegramBot;
    }

    /**
     * Метод добавления пользователя в БД
     */
    public boolean createUser(DialogDto dialogDto) {
        UserRecord userRecord = findUserByChatId(dialogDto.chatId());
        if (userRecord == null) {
            userRecord = new UserRecord(dialogDto.chatId(), dialogDto.name(), false);
            userRepository.save(recordMapper.toEntity(userRecord));
            return true;
        }
        return false;
    }

    /**
     * Метод записи пользователя в БД
     */
    public UserRecord createUser(UserRecord userRecord) {
        UserEntity userEntity = recordMapper.toEntity(userRecord);
        return recordMapper.toRecord(userRepository.save(userEntity));
    }

    /**
     * Метод находит всех пользователей в БД
     *
     * @return возвращает List пользователей
     */
    public Collection<UserRecord> getAllUsers() {
        logger.info("Вызов метода получения всех пользователей из БД");
        return userRepository.findAll().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    /**
     * Метод находит пользователя по id
     *
     * @param id - id пользователя
     * @return возвращает найденного пользователя
     */
    public UserRecord findUserById(Long id) {
        logger.info("Вызов метода поиска пользователя по id");
        return recordMapper.toRecord(userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Не найден пользователь с id = {}", id);
                    return new UserNotFoundException(id);
                }));
    }

    /**
     * Метод находит пользователя по chatId
     *
     * @param chatId - чат пользователя
     * @return возвращает найденного пользователя
     */
    public UserRecord findUserByChatId(Long chatId) {
        logger.info("Вызов метода поиска пользователя по chatId");
        return recordMapper.toRecord(userRepository.findUserEntityByChatId(chatId)
                .orElse(null));
    }

    /**
     * Метод находит все отчеты пользователя
     *
     * @param id - id пользователя
     * @return возвращает List отчетов пользователя
     */
    public Collection<ReportRecord> findReportsByUser(Long id) {
        logger.info("Вызов метода поиска отчетов пользователя");
        return userRepository.findById(id)
                .map(UserEntity::getReportEntity)
                .map(reports ->
                        reports.stream()
                                .map(recordMapper::toRecord)
                                .collect(Collectors.toList()))
                .orElseThrow(() -> {
                    logger.error("Не найден пользователь с id = {}", id);
                    return new UserNotFoundException(id);
                });
    }

    /**
     * Метод добавляет пользователю животное
     *
     * @param id       - id пользователя
     * @param animalId - id животного
     * @return возвращает пользователя, который забрал животное из приюта
     */
    public UserRecord patchUserAnimal(Long id, Long animalId) {
        logger.info("Вызов метода добавления животного пользователю");
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        Optional<AnimalEntity> optionalAnimal = animalRepository.findById(animalId);
        if (optionalUser.isEmpty()) {
            logger.error("Не найден пользователь с id = {}", id);
            throw new UserNotFoundException(id);
        }
        if (optionalAnimal.isEmpty()) {
            logger.error("Не найдено животное с id = {}", animalId);
            throw new AnimalNotFoundException(animalId);
        }
        UserEntity userEntity = optionalUser.get();
        userEntity.setAnimalEntity(optionalAnimal.get());
        return recordMapper.toRecord(userRepository.save(userEntity));
    }

    /**
     * Метод удаляет пользователя из БД
     *
     * @param id - id пользователя
     * @return возвращает удаленного пользователя
     */
    public UserRecord deleteUser(Long id) {
        logger.info("Вызов метода удаления пользователя");
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Не найден пользователь с id = {}", id);
                    return new UserNotFoundException(id);
                });
        userRepository.delete(userEntity);
        return recordMapper.toRecord(userEntity);
    }

    /**
     * Метод увеличения испытательного срока пользователю
     *
     * @param id - id пользователя
     * @return возвращает пользователя, с продленным испытательным сроком
     */
    public UserRecord extendPeriod(Long id, Integer days) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Не найден пользователь с id = {}", id);
                    return new UserNotFoundException(id);
                });
        if (user.getDate() == null) {
            throw new DateNotFoundException(id);
        }
        LocalDateTime localDateTime = user.getDate();
        if (days > 0) {
            user.setDate(localDateTime.plusDays(days));
            logger.info("Продление испытательного срока на " + days + " пользователю с id = {}", id);
        } else {
            logger.error("Не верно указано количество дней для продления испытательного срока.");
        }
        return recordMapper.toRecord(userRepository.save(user));
    }

    public void sendMessage(Long Id, String text) {
        Optional<UserEntity> user = userRepository.findById(Id);
        if (user.isPresent()) {
            SendMessage preparedMessage = new SendMessage(user.get().getChatId(), text);
            telegramBot.execute(preparedMessage);
        }
    }


    /**
     * Метод начинает чат пользователя с волонтером
     *
     * @param userChatId - chatId вызывающего пользователя
     */
    public void openChat(Long userChatId) {
        Long volunteerChatId = findVolunteerChatId(userChatId);
        UserEntity userEntity = userRepository.getUserEntitiesByChatId(userChatId);
        userEntity.setVolunteerChatId(volunteerChatId);
        userRepository.save(userEntity);
    }

    public Long findVolunteerChatId(Long userChatId) {
        List<UserEntity> volunteers = userRepository.findUserEntitiesByIsVolunteer(true);
        if (volunteers.size() == 0) {
            throw new VolunteerNotFoundException(userChatId);
        }
        return volunteers.get((int) (Math.random() * volunteers.size())).getChatId();
    }

    public void closeChat(Long userChatId) {
        UserEntity userEntity = userRepository.getUserEntitiesByChatId(userChatId);
        userEntity.setVolunteerChatId(null);
        userRepository.save(userEntity);
    }
}
