package pro.sky.shelter.core.record;

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
    private AnimalRecord animalRecord;
    private List<ReportRecord> reportRecord;
}
