package pro.sky.shelter.configuration;

import com.github.javafaker.Faker;
import pro.sky.shelter.core.model.AnimalType;
import pro.sky.shelter.core.record.AnimalRecord;
import pro.sky.shelter.core.record.ReportRecord;
import pro.sky.shelter.core.record.UserRecord;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Generator {
    private final Faker faker = new Faker();

    public UserRecord generateUser() {
        UserRecord userRecord = new UserRecord();
        userRecord.setChatId(777L);
        userRecord.setUserName(faker.name().firstName());
        userRecord.setPhone(faker.phoneNumber().toString());
        userRecord.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        return userRecord;
    }

    public UserRecord generateUser(AnimalRecord animalRecord) {
        UserRecord userRecord = new UserRecord();
        userRecord.setChatId(faker.number().randomNumber());
        userRecord.setUserName(faker.name().firstName());
        userRecord.setPhone(faker.phoneNumber().toString());
        userRecord.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        if (animalRecord != null) {
            userRecord.setAnimalRecord(animalRecord);
        }
        return userRecord;
    }

    public AnimalRecord generateAnimal() {
        int number = faker.random().nextInt(1, 2);
        AnimalRecord animalRecord = new AnimalRecord();
        if (number == 1) {
            animalRecord.setAnimalType(AnimalType.CAT);
        } else {
            animalRecord.setAnimalType(AnimalType.DOG);
        }
        animalRecord.setAnimalName(faker.animal().name());
        return animalRecord;
    }

    public ReportRecord generateReport(UserRecord userRecord) {
        ReportRecord reportRecord = new ReportRecord();
        reportRecord.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        reportRecord.setDiet(faker.book().title());
        reportRecord.setHealth(faker.book().title());
        reportRecord.setBehavior(faker.book().title());
        if (userRecord != null) {
            reportRecord.setUserRecord(userRecord);
        }
        return reportRecord;
    }
}
