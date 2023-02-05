package pro.sky.shelter.core.record;

import pro.sky.shelter.core.model.AnimalType;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс хранения информации о животном
 */
@Getter
@Setter
public class AnimalRecord {
    private long animal_id;
    private AnimalType animalType;
    private String animalName;
}
