package pro.sky.shelter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.shelter.core.entity.AnimalEntity;
import pro.sky.shelter.core.exception.AnimalNotFoundException;
import pro.sky.shelter.core.record.AnimalRecord;
import pro.sky.shelter.core.record.RecordMapper;
import pro.sky.shelter.core.repository.AnimalRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class AnimalService {
    private final Logger logger = LoggerFactory.getLogger(AnimalService.class);
    private final AnimalRepository animalRepository;
    private final RecordMapper recordMapper;

    public AnimalService(AnimalRepository animalRepository, RecordMapper recordMapper) {
        this.animalRepository = animalRepository;
        this.recordMapper = recordMapper;
    }

    /**
     * Метод создает животное в БД
     *
     * @param animalRecord - животное
     * @return возвращает животное которое создал
     */
    public AnimalRecord createAnimal(AnimalRecord animalRecord) {
        logger.info("Вызов метода создания животного");
        AnimalEntity animalEntity = recordMapper.toEntity(animalRecord);
        return recordMapper.toRecord(animalRepository.save(animalEntity));
    }

    /**
     * Метод находит всех животных в БД
     *
     * @return возвращает List животных
     */
    public Collection<AnimalRecord> getAllAnimal() {
        logger.info("Вызов метода getAllAnimals");
        return animalRepository.findAll().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    /**
     * Метод находит животное по id
     *
     * @param id - id животного
     * @return возвращает животное которое нашел
     */
    public AnimalRecord findAnimal(Long id) {
        logger.info("Вызов метода поиска животного");
        return recordMapper.toRecord(animalRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Не найдено животное с id = {}", id);
                    return new AnimalNotFoundException(id);
                }));
    }

    /**
     * Метод изменяет параметры животного
     *
     * @param id           - id животного
     * @param animalRecord - животное
     * @return возвращает животное
     */
    public AnimalRecord editAnimal(Long id, AnimalRecord animalRecord) {
        logger.info("Вызов метода редактирования животного");
        AnimalEntity oldAnimal = animalRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Не найдено животное с id = {}", id);
                    return new AnimalNotFoundException(id);
                });
        oldAnimal.setAnimalName(animalRecord.getAnimalName());
        oldAnimal.setAnimalType(animalRecord.getAnimalType());
        return recordMapper.toRecord(animalRepository.save(oldAnimal));
    }

    /**
     * Метод удаляет животное из БД
     *
     * @param id - id животного
     * @return возвращает удаленное животное
     */
    public AnimalEntity deleteAnimal(Long id) {
        logger.info("Вызов метода удаления животного");
        AnimalEntity animalEntity = animalRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Не найдено животное с id = {}", id);
                    return new AnimalNotFoundException(id);
                });
        animalRepository.delete(animalEntity);
        return animalEntity;
    }
}
