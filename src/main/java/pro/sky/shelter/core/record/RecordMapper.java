package pro.sky.shelter.core.record;

import org.springframework.stereotype.Component;
import pro.sky.shelter.core.entity.AnimalEntity;
import pro.sky.shelter.core.entity.AnimalPhotoEntity;
import pro.sky.shelter.core.entity.ReportEntity;
import pro.sky.shelter.core.entity.UserEntity;

/**
 * Класс конвертации из Entity в Record и обратно
 */
@Component
public class RecordMapper {
    public UserRecord toRecord(UserEntity userEntity) {
        if (userEntity == null) return null;
        UserRecord userRecord = new UserRecord();
        userRecord.setId(userEntity.getId());
        userRecord.setChatId(userEntity.getChatId());
        userRecord.setUserName(userEntity.getUserName());
        userRecord.setPhone(userEntity.getPhone());
        userRecord.setDate(userEntity.getDate());
        if (userEntity.getAnimalEntity() != null) {
            userRecord.setAnimalRecord(toRecord(userEntity.getAnimalEntity()));
        }
        return userRecord;
    }

    public AnimalRecord toRecord(AnimalEntity animal) {
        AnimalRecord animalRecord = new AnimalRecord();
        animalRecord.setAnimal_id(animal.getId());
        animalRecord.setAnimalType(animal.getAnimalType());
        animalRecord.setAnimalName(animal.getAnimalName());

        return animalRecord;
    }

    public AnimalPhotoRecord toRecord(AnimalPhotoEntity animalPhotoEntity) {
        return new AnimalPhotoRecord(
                animalPhotoEntity.getId(),
                animalPhotoEntity.getMediaType()
        );
    }

    public ReportRecord toRecord(ReportEntity reportEntity) {
        ReportRecord reportRecord = new ReportRecord();
        reportRecord.setId(reportEntity.getId());
        reportRecord.setAnimalName(reportEntity.getAnimalName());
        reportRecord.setDate(reportEntity.getDate());
        reportRecord.setDiet(reportEntity.getDiet());
        reportRecord.setHealth(reportEntity.getHealth());
        reportRecord.setBehavior(reportEntity.getBehavior());
        if (reportEntity.getUserEntity() != null) {
            reportRecord.setUserRecord(toRecord(reportEntity.getUserEntity()));
        }
        if (reportEntity.getAnimalPhotoEntity() != null) {
            reportRecord.setAnimalPhotoRecord(toRecord(reportEntity.getAnimalPhotoEntity()));
        }
        return reportRecord;
    }

    public AnimalEntity toEntity(AnimalRecord animalRecord) {
        AnimalEntity animal = new AnimalEntity();
        animal.setAnimalType(animalRecord.getAnimalType());
        animal.setAnimalName(animalRecord.getAnimalName());
        return animal;
    }

    public UserEntity toEntity(UserRecord userRecord) {
        UserEntity userEntity = new UserEntity(userRecord.getChatId(), userRecord.getUserName(), userRecord.getPhone());
        if (userRecord.getDate() != null) {
            userEntity.setDate(userRecord.getDate());
        }
        if (userRecord.getAnimalRecord() != null) {
            AnimalEntity animalEntity = toEntity(userRecord.getAnimalRecord());
            animalEntity.setId(userRecord.getAnimalRecord().getAnimal_id());
            userEntity.setAnimalEntity(animalEntity);
        } else {
            userEntity.setAnimalEntity(null);
        }
        return userEntity;
    }

    public ReportEntity toEntity(ReportRecord reportRecord) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setId(reportRecord.getId());
        reportEntity.setAnimalName(reportRecord.getAnimalName());
        reportEntity.setDate(reportRecord.getDate());
        reportEntity.setDiet(reportRecord.getDiet());
        reportEntity.setHealth(reportRecord.getHealth());
        reportEntity.setBehavior(reportRecord.getBehavior());
        if (reportRecord.getUserRecord() != null) {
            reportEntity.setUserEntity(toEntity(reportRecord.getUserRecord()));
        }
        return reportEntity;
    }
}
