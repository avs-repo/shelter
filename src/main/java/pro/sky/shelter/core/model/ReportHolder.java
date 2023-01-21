package pro.sky.shelter.core.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import pro.sky.shelter.core.entity.AnimalPhotoEntity;
import pro.sky.shelter.core.entity.UserEntity;

/**
 * Держим данные отчета пока он публикуется
 */
@Getter
@Setter
public class ReportHolder {
    private LocalDateTime date;
    private String animalName;
    private String diet;
    private String health;
    private String behavior;

    private AnimalPhotoEntity animalPhotoEntity;
    private UserEntity userEntity;
}
