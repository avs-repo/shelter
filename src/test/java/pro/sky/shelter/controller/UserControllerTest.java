package pro.sky.shelter.controller;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pro.sky.shelter.core.entity.ReportEntity;
import pro.sky.shelter.core.model.AnimalType;
import pro.sky.shelter.core.record.AnimalRecord;
import pro.sky.shelter.core.record.RecordMapper;
import pro.sky.shelter.core.record.ReportRecord;
import pro.sky.shelter.core.record.UserRecord;
import pro.sky.shelter.core.repository.AnimalRepository;
import pro.sky.shelter.core.repository.ReportRepository;
import pro.sky.shelter.core.repository.UserRepository;
import pro.sky.shelter.core.repository.VolunteerRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VolunteerRepository volunteerRepository;
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private RecordMapper recordMapper;
    private final Faker faker = new Faker();

    @AfterEach
    public void afterEach() {
        reportRepository.deleteAll();
        userRepository.deleteAll();
        animalRepository.deleteAll();
        volunteerRepository.deleteAll();
    }

    @Test
    public void createTest() {
        addUser(generateUser());
    }

    @Test
    public void putTest() {
        UserRecord userRecord = addUser(generateUser());
        String userName = userRecord.getUserName();

        ResponseEntity<UserRecord> getRecordResponseEntity = testRestTemplate.getForEntity("http://localhost:" + port + "/user/" + userRecord.getId(), UserRecord.class);
        assertThat(getRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getRecordResponseEntity.getBody()).isNotNull();
        assertThat(getRecordResponseEntity.getBody()).usingRecursiveComparison().isEqualTo(userRecord);
        assertThat(getRecordResponseEntity.getBody().getUserName()).isEqualTo(userName);

        userRecord.setUserName("Gates");

        ResponseEntity<UserRecord> recordResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/user/" + userRecord.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(userRecord),
                UserRecord.class
        );

        assertThat(recordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(recordResponseEntity.getBody()).isNotNull();
        assertThat(recordResponseEntity.getBody()).usingRecursiveComparison().isEqualTo(userRecord);
        assertThat(recordResponseEntity.getBody().getUserName()).isEqualTo("Gates");
    }

    @Test
    public void findTests() {
        List<UserRecord> volunteerRecords = Stream.generate(this::generateUser)
                .limit(10)
                .map(this::addUser)
                .toList();
        List<AnimalRecord> animalRecords = Stream.generate(() -> generateAnimal(volunteerRecords.get(faker.random().nextInt(volunteerRecords.size()))))
                .limit(30)
                .map(this::addAnimal)
                .toList();
        List<UserRecord> userRecords = Stream.generate(() -> generateUser(animalRecords.get(faker.random().nextInt(animalRecords.size()))))
                .limit(10)
                .toList();
        List<ReportRecord> reportRecords = Stream.generate(() -> generateReport(userRecords.get(faker.random().nextInt(userRecords.size()))))
                .limit(50)
                .toList();
        UserRecord userRecord = userRecords.get(0);
        List<ReportRecord> expected = reportRecords.stream()
                .filter(r -> r.getUserRecord().getId().equals(userRecord.getId()))
                .toList();

        ResponseEntity<List<UserRecord>> getAllUsersResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/user/",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                });

        assertThat(getAllUsersResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getAllUsersResponseEntity.getBody())
                .hasSize(userRecords.size())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(userRecords);

        ResponseEntity<List<ReportRecord>> getReportsResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/user/" + userRecord.getId() + "/reports/",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                });

        assertThat(getReportsResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getReportsResponseEntity.getBody()).isNotNull();
        assertThat(getReportsResponseEntity.getBody()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void findUser() {
        UserRecord volunteerRecord = addUser(generateUser());
        AnimalRecord animalRecord = addAnimal(generateAnimal(volunteerRecord));
        UserRecord userRecord = generateUser(animalRecord);

        ResponseEntity<UserRecord> getRecordResponseEntity = testRestTemplate.getForEntity("http://localhost:" + port + "/user/" + userRecord.getId(), UserRecord.class);

        assertThat(getRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getRecordResponseEntity.getBody()).isNotNull();
        assertThat(getRecordResponseEntity.getBody()).usingRecursiveComparison().isEqualTo(userRecord);
        assertThat(getRecordResponseEntity.getBody().getAnimal()).usingRecursiveComparison().isEqualTo(animalRecord);

        ResponseEntity<UserRecord> getByAnimalRecordResponseEntity = testRestTemplate.getForEntity("http://localhost:" + port + "/user/animal/" + animalRecord.getAnimal_id(), UserRecord.class);

        assertThat(getByAnimalRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getByAnimalRecordResponseEntity.getBody()).isNotNull();
        assertThat(getByAnimalRecordResponseEntity.getBody()).usingRecursiveComparison().isEqualTo(userRecord);
        assertThat(getByAnimalRecordResponseEntity.getBody().getAnimal()).usingRecursiveComparison().isEqualTo(animalRecord);
    }

    @Test
    public void deleteTest() {
        UserRecord userRecord = addUser(generateUser());

        ResponseEntity<UserRecord> recordResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/volunteer/" + userRecord.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(userRecord),
                UserRecord.class
        );

        assertThat(recordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(recordResponseEntity.getBody()).isNotNull();
        assertThat(recordResponseEntity.getBody()).usingRecursiveComparison().isEqualTo(userRecord);
        assertThat(recordResponseEntity.getBody().getUserName()).isEqualTo(userRecord.getUserName());
    }

    @Test
    public void patchUserAnimal() {
        UserRecord user = addUser(generateUser());
        AnimalRecord animalRecord = addAnimal(generateAnimal(user));
        UserRecord userRecord = generateUser(animalRecord);
        userRecord.setDate(userRecord.getDate().plusMonths(1));
        userRecord.setAnimal(animalRecord);

        ResponseEntity<UserRecord> recordResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/user/" + userRecord.getId() + "/animal?animalId=" + animalRecord.getAnimal_id(),
                HttpMethod.PATCH,
                new HttpEntity<>(userRecord),
                UserRecord.class
        );

        assertThat(recordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(recordResponseEntity.getBody()).isNotNull();
        assertThat(recordResponseEntity.getBody()).usingRecursiveComparison().isEqualTo(userRecord);
        assertThat(recordResponseEntity.getBody().getAnimal()).usingRecursiveComparison().isEqualTo(animalRecord);
    }

    @Test
    public void patchExtensionPeriodUserTest() {
        UserRecord volunteerRecord = addUser(generateUser());
        AnimalRecord animalRecord = addAnimal(generateAnimal(volunteerRecord));
        UserRecord userRecord = generateUser(animalRecord);
        userRecord.setDate(userRecord.getDate().plusDays(14));

        ResponseEntity<UserRecord> recordResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/user/" + userRecord.getId() + "/period?number=" + 14,
                HttpMethod.PATCH,
                new HttpEntity<>(userRecord),
                UserRecord.class
        );

        assertThat(recordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(recordResponseEntity.getBody()).isNotNull();
        assertThat(recordResponseEntity.getBody()).usingRecursiveComparison().isEqualTo(userRecord);
        assertThat(recordResponseEntity.getBody().getAnimal()).usingRecursiveComparison().isEqualTo(animalRecord);
    }

    @Test
    public void sendMessageToUserTest() {
        UserRecord volunteerRecord = addUser(generateUser());
        AnimalRecord animalRecord = addAnimal(generateAnimal(volunteerRecord));
        UserRecord userRecord = generateUser(animalRecord);

        ResponseEntity<String> getRecordResponseEntity = testRestTemplate.getForEntity("http://localhost:" + port + "/volunteer/user/" + userRecord.getId() + "/decision?number=1", String.class);

        assertThat(getRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getRecordResponseEntity.getBody()).isNotNull();
        assertThat(getRecordResponseEntity.getBody()).isEqualTo("Сообщение отправлено");
    }

    private UserRecord generateUser(AnimalRecord animalRecord) {
        UserRecord userRecord = new UserRecord();
        userRecord.setChatId(faker.number().randomNumber());
        userRecord.setUserName(faker.name().firstName());
        userRecord.setPhone(faker.phoneNumber().toString());
        userRecord.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        if (animalRecord != null) {
            userRecord.setAnimalRecord(animalRecord);
        }
        return recordMapper.toRecord(userRepository.save(recordMapper.toEntity(userRecord)));
    }

    private AnimalRecord generateAnimal(UserRecord userRecord) {
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

    private UserRecord generateUser() {
        UserRecord userRecord = new UserRecord();
        userRecord.setUserName(faker.name().firstName());
        userRecord.setPhone(faker.phoneNumber().toString());
        return userRecord;
    }

    private ReportRecord generateReport(UserRecord userRecord) {
        ReportRecord reportRecord = new ReportRecord();
        reportRecord.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        reportRecord.setDiet(faker.book().title());
        reportRecord.setHealth(faker.book().title());
        reportRecord.setBehavior(faker.book().title());
        if (userRecord != null) {
            reportRecord.setUserRecord(userRecord);
        }
        ReportEntity report = recordMapper.toEntity(reportRecord);
        return recordMapper.toRecord(reportRepository.save(report));
    }

    private AnimalRecord addAnimal(AnimalRecord animalRecord) {
        ResponseEntity<AnimalRecord> animalResponseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/animal", animalRecord, AnimalRecord.class);
        assertThat(animalResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(animalResponseEntity.getBody()).isNotNull();
        assertThat(animalResponseEntity.getBody()).usingRecursiveComparison().ignoringFields("animal_id").isEqualTo(animalRecord);

        return animalResponseEntity.getBody();
    }

    private UserRecord addUser(UserRecord userRecord) {
        ResponseEntity<UserRecord> userResponseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/user", userRecord, UserRecord.class);
        assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userResponseEntity.getBody()).isNotNull();
        assertThat(userResponseEntity.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(userRecord);
        assertThat(userResponseEntity.getBody().getId()).isNotNull();

        return userResponseEntity.getBody();
    }

}