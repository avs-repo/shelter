package pro.sky.shelter.core.record;

import jakarta.validation.constraints.NotNull;
import pro.sky.shelter.core.model.AnimalType;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс хранения информации о животном
 */
@Getter
@Setter
public class AnimalRecord {
    private long id;
    private AnimalType animalType;
    @NotNull(message = "Необходимо имя животного!")
    private String animalName;
}
