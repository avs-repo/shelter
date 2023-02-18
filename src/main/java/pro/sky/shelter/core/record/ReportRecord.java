package pro.sky.shelter.core.record;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * Рекорд отчета
 */
@Getter
@Setter
public class ReportRecord {
    private Long id;
    private LocalDateTime date;
    private String animalName;
    private String diet;
    private String health;
    private String behavior;

    private AnimalPhotoRecord animalPhotoRecord;
    private UserRecord userRecord;
}
