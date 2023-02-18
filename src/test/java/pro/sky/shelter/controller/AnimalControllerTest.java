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
import org.springframework.test.context.ActiveProfiles;
import pro.sky.shelter.configuration.Generator;
import pro.sky.shelter.core.record.AnimalRecord;
import pro.sky.shelter.core.record.RecordMapper;

import pro.sky.shelter.core.repository.AnimalRepository;


import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnimalControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private RecordMapper recordMapper;

    private final Faker faker = new Faker();
    private final Generator generator = new Generator();

    @AfterEach
    public void afterEach() {

        animalRepository.deleteAll();
    }

    @Test
    public void createAnimalTest() {
        addAnimal(generator.generateAnimal());
    }

    @Test
    public void putTest() {

        List<AnimalRecord> animalRecords = Stream.generate(generator::generateAnimal)
                .limit(10)
                .map(this::addAnimal)
                .toList();

        AnimalRecord animalRecord = animalRecords.get(0);

        ResponseEntity<AnimalRecord> recordResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/animal/"+animalRecord.getAnimal_id(),
                HttpMethod.PUT,
                new HttpEntity<>(animalRecord),
                AnimalRecord.class
        );
    }


    @Test
    public void findTests() {
        List<AnimalRecord> animalRecords = Stream.generate(generator::generateAnimal)
                .limit(10)
                .map(this::addAnimal)
                .toList();

        AnimalRecord animalRecord = animalRecords.get(0);

        ResponseEntity<List<AnimalRecord>> getAnimalsResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/animal/",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                });

        assertThat(getAnimalsResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getAnimalsResponseEntity.getBody())
                .hasSize(animalRecords.size())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(animalRecords);

    }

    @Test
    public void deleteTest() {

        List<AnimalRecord> animalRecords = Stream.generate(generator::generateAnimal)
                .limit(10)
                .map(this::addAnimal)
                .toList();

        AnimalRecord animalRecord = animalRecords.get(0);

        ResponseEntity<AnimalRecord> recordResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/animal/" + animalRecord.getAnimal_id(),
                HttpMethod.DELETE,
                new HttpEntity<>(animalRecord),
                AnimalRecord.class
        );

        assertThat(recordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(recordResponseEntity.getBody()).isNotNull();
        assertThat(recordResponseEntity.getBody()).usingRecursiveComparison().isEqualTo(animalRecord);

    }

    private AnimalRecord addAnimal(AnimalRecord animalRecord) {
        ResponseEntity<AnimalRecord> animalResponseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/animal", animalRecord, AnimalRecord.class);
        assertThat(animalResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(animalResponseEntity.getBody()).isNotNull();
        assertThat(animalResponseEntity.getBody()).usingRecursiveComparison().ignoringFields("animal_id").isEqualTo(animalRecord);

        animalRepository.save(recordMapper.toEntity(animalRecord));
        return animalResponseEntity.getBody();
    }


}
