package pro.sky.shelter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.shelter.core.dto.DialogDto;
import pro.sky.shelter.core.entity.AnimalEntity;
import pro.sky.shelter.core.exception.AnimalNotFoundException;
import pro.sky.shelter.core.exception.UserNotFoundException;
import pro.sky.shelter.core.record.RecordMapper;
import pro.sky.shelter.core.record.ReportRecord;
import pro.sky.shelter.core.record.UserRecord;
import pro.sky.shelter.core.entity.UserEntity;
import pro.sky.shelter.core.repository.AnimalRepository;
import pro.sky.shelter.core.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final RecordMapper recordMapper;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, AnimalRepository animalRepository, RecordMapper recordMapper) {
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
        this.recordMapper = recordMapper;
    }

    /**
     * Метод добавления пользователя в БД
     *
     * @return возвращает рекорд созданного пользователя
     */
    public UserRecord createUser(DialogDto dialogDto) {
        UserRecord userRecord = findUserByChatId(dialogDto.chatId());
        if (userRecord == null) {
            userRecord = new UserRecord();
            userRecord.setChatId(dialogDto.chatId());
            userRecord.setUserName(dialogDto.name());
            userRepository.save(recordMapper.toEntity(userRecord));
        }
        return userRecord;
    }

    /**
     * Метод находит всех пользователей в БД
     *
     * @return возвращает список пользователей
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
     * @param id id пользователя
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

    public UserRecord findUserByChatId(Long chatId) {
        logger.info("Вызов метода поиска пользователя по chatId");
        return recordMapper.toRecord(userRepository.findUserEntityByChatId(chatId)
                .orElseThrow(() -> {
                    logger.error("Не найден пользователь с id = {}", chatId);
                    return new UserNotFoundException(chatId);
                }));
    }

    /**
     * Метод находит все отчеты пользователя
     *
     * @param id id пользователя
     * @return возвращает список отчетов
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
     * @param id       id пользователя
     * @param animalId id животного
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
     * Метод ищет пользователя по животному
     *
     * @param animalId - id животного
     * @return возвращает пользователя
     */
    public UserRecord findUserByAnimal(long animalId) {
        logger.info("Вызов метода поиска пользователя по животному");
        AnimalEntity animalEntity = animalRepository.findById(animalId)
                .orElseThrow(() -> {
                    logger.error("Не найдено животное с id = {}", animalId);
                    return new AnimalNotFoundException(animalId);
                });
        return recordMapper.toRecord(userRepository.findByAnimalEntity(animalEntity));
    }

    /**
     * Метод удаляет пользователя из БД
     *
     * @param id id пользователя
     * @return возвращает удаленного пользователя
     */
    public UserEntity deleteUser(Long id) {
        logger.info("Вызов метода удаления пользователя");
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Не найден пользователь с id = {}", id);
                    return new UserNotFoundException(id);
                });
        userRepository.delete(userEntity);
        return userEntity;
    }
}
