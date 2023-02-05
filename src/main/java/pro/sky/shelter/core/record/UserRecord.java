package pro.sky.shelter.core.record;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Рекорд пользователя
 */
@Getter
@Setter
public class UserRecord {
    private Long id;
    private String userName;
    private Long chatId;
    private String phone;
    private LocalDateTime date;
    private AnimalRecord animalRecord;
    private List<ReportRecord> reportRecord;

    public AnimalRecord getAnimalRecord() {
        return animalRecord;
    }

    public void setAnimalRecord(AnimalRecord animalRecord) {
        this.animalRecord = animalRecord;
    }
}
