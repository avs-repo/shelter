package pro.sky.shelter.service;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import pro.sky.shelter.configuration.Generator;
import pro.sky.shelter.core.record.AnimalRecord;
import pro.sky.shelter.core.record.RecordMapper;
import pro.sky.shelter.core.record.UserRecord;
import pro.sky.shelter.core.repository.AnimalPhotoRepository;
import pro.sky.shelter.core.repository.AnimalRepository;
import pro.sky.shelter.core.repository.ReportRepository;
import pro.sky.shelter.core.repository.UserRepository;
import pro.sky.shelter.listener.BotListener;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static pro.sky.shelter.configuration.BotConstants.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class BotServiceTest {

    @MockBean
    private TelegramBot telegramBot;
    @Autowired
    private BotService botService;
    @Autowired
    private UserService userService;

    @Autowired
    private ContentSaverService contentSaverService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private AnimalPhotoRepository animalPhotoRepository;
    @Autowired
    @InjectMocks
    private BotListener botListener;

    private final Generator generator = new Generator();
    private final RecordMapper recordMapper = new RecordMapper();

    @Test
    void contextLoads() {
        assertThat(botService).isNotNull();
        assertThat(botListener).isNotNull();
        assertThat(telegramBot).isNotNull();
        assertThat(userRepository).isNotNull();
        assertThat(animalRepository).isNotNull();
        assertThat(reportRepository).isNotNull();
        assertThat(animalPhotoRepository).isNotNull();
    }

    @AfterEach
    public void clearData() {
        userRepository.deleteAll();
        animalRepository.deleteAll();
        reportRepository.deleteAll();
        animalPhotoRepository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("paramsForWelcomeMenu")
    public void startDialogTest(String text) throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(Objects.requireNonNull(BotServiceTest.class.getResource("message.json")).toURI()));
        Update update = getUpdate(json, text);

        botService.process(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(777L);
        assertThat(actual.getParameters().get("reply_markup")).isNotNull();
        assertThat(actual.getParameters().get("text")).isEqualTo(GREETING_MSG);
    }

    @Test
    public void reportTextTest() throws URISyntaxException, IOException {
        String jsonText = Files.readString(Paths.get(Objects.requireNonNull(BotServiceTest.class.getResource("message.json")).toURI()));

        AnimalRecord animalRecord = addAnimal(generator.generateAnimal());
        assertThat(animalRecord).isNotNull();

        UserRecord userRecord = addUser(generator.generateUser());
        userService.patchUserAnimal(1L, 1L);
        assertThat(userRecord).isNotNull();

        List<Update> updateList = new ArrayList<>(List.of(
                getUpdate(jsonText, SEND_REPORT_CMD),
                getUpdate(jsonText, "1) Корм 2) Хорошее 3) Изменений в поведении нет")));

        botService.process(updateList.get(0));
        botService.process(updateList.get(1));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        List<SendMessage> actualList = argumentCaptor.getAllValues();
        Assertions.assertThat(actualList.size()).isEqualTo(2);

        assertThat(actualList.get(0).getParameters().get("chat_id")).isEqualTo(777L);
        assertThat(actualList.get(0).getParameters().get("text")).isEqualTo(SEND_REPORT_MSG);

        assertThat(actualList.get(1).getParameters().get("chat_id")).isEqualTo(777L);
        assertThat(actualList.get(1).getParameters().get("text")).isEqualTo("Спасибо, теперь отправьте фото животного.");
    }

    private Update getUpdate(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%command%", replaced), Update.class);
    }

    private static Stream<Arguments> paramsForWelcomeMenu() {
        return Stream.of(
                Arguments.of(INITIAL_CMD),
                Arguments.of(GO_BACK_CMD)
        );
    }

    private UserRecord addUser(UserRecord userRecord) {
        if (userRecord != null) {
            System.out.println(userRecord);
            userRepository.save(recordMapper.toEntity(userRecord));
        }
        return userRecord;
    }

    private AnimalRecord addAnimal(AnimalRecord animalRecord) {
        if (animalRecord != null) {
            System.out.println(animalRecord);
            animalRepository.save(recordMapper.toEntity(animalRecord));
        }
        return animalRecord;
    }
}