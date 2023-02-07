package pro.sky.shelter.controller;

import com.github.javafaker.Faker;
import com.pengrad.telegrambot.model.User;
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
import org.springframework.test.context.ActiveProfiles;
import pro.sky.shelter.configuration.Generator;
import pro.sky.shelter.core.model.AnimalType;
import pro.sky.shelter.core.record.AnimalRecord;
import pro.sky.shelter.core.record.RecordMapper;
import pro.sky.shelter.core.record.ReportRecord;
import pro.sky.shelter.core.record.UserRecord;
import pro.sky.shelter.core.repository.AnimalRepository;
import pro.sky.shelter.core.repository.ReportRepository;
import pro.sky.shelter.core.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
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
    private AnimalRepository animalRepository;
    @Autowired
    private RecordMapper recordMapper;

    private final Faker faker = new Faker();
    private final Generator generator = new Generator();

    @AfterEach
    public void afterEach() {
        reportRepository.deleteAll();
        userRepository.deleteAll();
        animalRepository.deleteAll();
    }

    @Test
    public void createUserTest() {
        addUser(generator.generateUser());
    }

    @Test
    public void getAllUsersTest() {
        List<UserRecord> userRecords = Stream.generate(generator::generateUser)
                .limit(10)
                .map(this::addUser)
                .toList();

        UserRecord userRecord = userRecords.get(0);
        ResponseEntity<List<UserRecord>> recordResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/user/",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                }
        );
    }

    @Test
    public void findTests() {
        List<AnimalRecord> animalRecords = Stream.generate(generator::generateAnimal)
                .limit(10)
                .map(this::addAnimal)
                .toList();
        List<UserRecord> userRecords = Stream.generate(() -> generator.generateUser(animalRecords.get(faker.random().nextInt(animalRecords.size()))))
                .limit(10)
                .map(this::addUser)
                .toList();
        List<ReportRecord> reportRecords = Stream.generate(() -> generator.generateReport(userRecords.get(faker.random().nextInt(userRecords.size()))))
                .limit(50)
                .toList();
        UserRecord userRecord = userRecords.get(0);
        List<ReportRecord> expected = reportRecords.stream()
                .filter(r -> r.getUserRecord().getId().equals(userRecord.getId()))
                .toList();

        ResponseEntity<List<UserRecord>> getAllUsersResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/user/",
                HttpMethod.GET,
                null,
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
    public void findUserByIdTest() {
        AnimalRecord animalRecord = addAnimal(generator.generateAnimal());
        UserRecord userRecordTest = addUser(generator.generateUser(animalRecord));

        ResponseEntity<UserRecord> getRecordResponseEntity = testRestTemplate.getForEntity("http://localhost:" + port + "/user/" + userRecordTest.getId(), UserRecord.class);

        assertThat(getRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getRecordResponseEntity.getBody()).isNotNull();
        assertThat(getRecordResponseEntity.getBody()).usingRecursiveComparison().isEqualTo(userRecordTest);
        assertThat(getRecordResponseEntity.getBody().getAnimalRecord()).usingRecursiveComparison().isEqualTo(animalRecord);
    }

    @Test
    public void deleteTest() {
        UserRecord userRecord = addUser(generator.generateUser());

        ResponseEntity<UserRecord> recordResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/user/" + userRecord.getId(),
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
    public void patchUserAnimalTest() {
        AnimalRecord animalRecord = addAnimal(generator.generateAnimal());
        UserRecord userRecord = addUser(generator.generateUser(animalRecord));

        ResponseEntity<UserRecord> recordResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/user/" + userRecord.getId() + "/animal?animalId=" + animalRecord.getAnimal_id(),
                HttpMethod.PATCH,
                new HttpEntity<>(userRecord),
                UserRecord.class
        );

        assertThat(recordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(recordResponseEntity.getBody()).isNotNull();
        assertThat(recordResponseEntity.getBody()).usingRecursiveComparison().isEqualTo(userRecord);
        assertThat(recordResponseEntity.getBody().getAnimalRecord()).usingRecursiveComparison().isEqualTo(animalRecord);
    }

    @Test
    public void extendPeriodTest() {
        AnimalRecord animalRecord = addAnimal(generator.generateAnimal());
        UserRecord userRecord = addUser(generator.generateUser(animalRecord));
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
        assertThat(recordResponseEntity.getBody().getAnimalRecord()).usingRecursiveComparison().isEqualTo(animalRecord);
    }

    @Test
    public void sendMessageToUserTest() {
        AnimalRecord animalRecord = addAnimal(generator.generateAnimal());
        UserRecord userRecord = addUser(generator.generateUser(animalRecord));

        ResponseEntity<String> getRecordResponseEntity = testRestTemplate.getForEntity("http://localhost:" + port + "/user/" + userRecord.getId() + "/message?text=test", String.class);

        assertThat(getRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getRecordResponseEntity.getBody()).isNotNull();
        assertThat(getRecordResponseEntity.getBody()).isEqualTo("Сообщение пользователю отправлено");
    }

    private AnimalRecord addAnimal(AnimalRecord animalRecord) {
        ResponseEntity<AnimalRecord> animalResponseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/animal", animalRecord, AnimalRecord.class);
        assertThat(animalResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(animalResponseEntity.getBody()).isNotNull();
        assertThat(animalResponseEntity.getBody()).usingRecursiveComparison().ignoringFields("animal_id").isEqualTo(animalRecord);

        animalRepository.save(recordMapper.toEntity(animalRecord));
        return animalResponseEntity.getBody();
    }

    private UserRecord addUser(UserRecord userRecord) {
        ResponseEntity<UserRecord> userResponseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/user", userRecord, UserRecord.class);

        assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userResponseEntity.getBody()).isNotNull();
        assertThat(userResponseEntity.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(userRecord);
        assertThat(userResponseEntity.getBody().getId()).isNotNull();

        userRepository.save(recordMapper.toEntity(userRecord));
        return userResponseEntity.getBody();
    }
}