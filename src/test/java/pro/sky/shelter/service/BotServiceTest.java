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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.test.context.ActiveProfiles;
import pro.sky.shelter.configuration.Generator;
import pro.sky.shelter.core.entity.AnimalPhotoEntity;
import pro.sky.shelter.core.entity.ReportEntity;
import pro.sky.shelter.core.model.ReportHolder;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
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
    @MockBean
    private ContentSaverService contentSaverService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnimalRepository animalRepository;
    @MockBean
    private ReportRepository reportRepository;
    @Autowired
    private AnimalPhotoRepository animalPhotoRepository;
    @Autowired
    @InjectMocks
    private BotListener botListener;

    private final Generator generator = new Generator();
    private final RecordMapper recordMapper = new RecordMapper();
    private final Long chatId = 777L;

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
    public void welcomeDialogsTest(String text) throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(Objects.requireNonNull(BotServiceTest.class.getResource("message.json")).toURI()));
        botService.process(getUpdate(json, text));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(chatId);
        assertThat(actual.getParameters().get("reply_markup")).isNotNull();
        assertThat(actual.getParameters().get("text")).isEqualTo(GREETING_MSG);
    }

    @ParameterizedTest
    @MethodSource("paramsForUnknownAndNegativeRequests")
    public void unknownMessageAndNegativeTests(String text) throws URISyntaxException, IOException {
        String jsonText = Files.readString(Paths.get(Objects.requireNonNull(BotServiceTest.class.getResource("message.json")).toURI()));
        botService.process(getUpdate(jsonText, text));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(chatId);
        assertThat(actual.getParameters().get("reply_markup")).isNotNull();
        assertThat(actual.getParameters().get("text")).isEqualTo(PROBLEM_OCCURS_MSG);
    }

    @ParameterizedTest
    @MethodSource("paramsForShelterMenu")
    public void shelterDialogsTest(String text, String expected) throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(Objects.requireNonNull(BotServiceTest.class.getResource("message.json")).toURI()));
        AnimalPhotoEntity animalPhoto = new AnimalPhotoEntity();
        animalPhoto.setId(1L);
        when(contentSaverService.getPhoto(anyLong())).thenReturn(Pair.of("img.jpg", new byte[]{(byte) 0xe0, 0x4f, (byte) 0xd0, 0x20}));
        botService.process(getUpdate(json, text));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        SendMessage actual;
        if (Objects.equals(text, SHELTER_CONTACTS_CMD)) {
            Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
            List<SendMessage> actualList = argumentCaptor.getAllValues();
            Assertions.assertThat(actualList.size()).isEqualTo(2);
            actual = actualList.get(actualList.size()-1);
        } else {
            verify(telegramBot).execute(argumentCaptor.capture());
            actual = argumentCaptor.getValue();
        }
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(chatId);
        assertThat(actual.getParameters().get("reply_markup")).isNotNull();
        assertThat(actual.getParameters().get("text")).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("paramsForConsultingMenu")
    public void consultingDialogsTest(String text, String expected) throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(Objects.requireNonNull(BotServiceTest.class.getResource("message.json")).toURI()));
        botService.process(getUpdate(json, text));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(chatId);
        assertThat(actual.getParameters().get("reply_markup")).isNotNull();
        assertThat(actual.getParameters().get("text")).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("paramsForContactsSave")
    public void contactsSaveTest(String phone, String expected) throws URISyntaxException, IOException {
        String jsonText = Files.readString(Paths.get(Objects.requireNonNull(BotServiceTest.class.getResource("message.json")).toURI()));
        botService.process(getUpdate(jsonText, phone));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(chatId);
        assertThat(actual.getParameters().get("reply_markup")).isNotNull();
        assertThat(actual.getParameters().get("text")).isEqualTo(expected);
    }

    @Test
    public void reportSaveTest() throws URISyntaxException, IOException {
        String jsonText = Files.readString(Paths.get(Objects.requireNonNull(BotServiceTest.class.getResource("message.json")).toURI()));
        String jsonPhoto = Files.readString(Paths.get(Objects.requireNonNull(BotServiceTest.class.getResource("photo.json")).toURI()));

        AnimalRecord animalRecord = addAnimal(generator.generateAnimal());
        assertThat(animalRecord).isNotNull();
        assertThat(animalRepository.findAll().size()).isEqualTo(1);

        UserRecord userRecord = addUser(generator.generateUser());
        assertThat(userRecord).isNotNull();
        assertThat(userRepository.findAll().size()).isEqualTo(1);

        List<Update> updateList = new ArrayList<>(List.of(
                getUpdate(jsonText, SEND_REPORT_CMD),
                getUpdate(jsonText, "1) Корм 2) Хорошее 3) Изменений в поведении нет"),
                getUpdate(jsonPhoto, "AgADAgADw6gxG_mCPAjHE7knq2P_UUJfLyLw4AAgI")));

        AnimalPhotoEntity animalPhoto = new AnimalPhotoEntity();
        animalPhoto.setId(1L);
        ReportEntity report = new ReportEntity();
        report.setAnimalPhotoEntity(animalPhoto);
        when(contentSaverService.uploadPhoto(updateList.get(2))).thenReturn(animalPhoto);
        when(reportRepository.save(any())).thenReturn(report);

        botService.process(updateList.get(0));
        botService.process(updateList.get(1));
        userService.patchUserAnimal(userRepository.findAll().get(0).getId(), animalRepository.findAll().get(0).getId());
        botService.process(updateList.get(1));
        botService.process(updateList.get(2));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(4)).execute(argumentCaptor.capture());
        List<SendMessage> actualList = argumentCaptor.getAllValues();
        Assertions.assertThat(actualList.size()).isEqualTo(4);

        assertThat(actualList.get(0).getParameters().get("chat_id")).isEqualTo(chatId);
        assertThat(actualList.get(0).getParameters().get("text")).isEqualTo(SEND_REPORT_MSG);

        assertThat(actualList.get(1).getParameters().get("chat_id")).isEqualTo(chatId);
        assertThat(actualList.get(1).getParameters().get("text")).isEqualTo("Извините, вы еще не брали питомца из нашего приюта.");

        assertThat(actualList.get(2).getParameters().get("chat_id")).isEqualTo(chatId);
        assertThat(actualList.get(2).getParameters().get("text")).isEqualTo("Спасибо, теперь отправьте фото животного.");

        assertThat(actualList.get(3).getParameters().get("chat_id")).isEqualTo(chatId);
        assertThat(actualList.get(3).getParameters().get("text")).isEqualTo("Спасибо, ваш отчет принят!");
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


    public static Stream<Arguments> paramsForUnknownAndNegativeRequests() {
        return Stream.of(
                Arguments.of("Абра, швабра, кадабра!"),
                Arguments.of("1)Гаф 2/Мяф 3)Вжик"),
                Arguments.of("+7-789-312-33-00")
        );
    }

    private static Stream<Arguments> paramsForContactsSave() {
        return Stream.of(
                Arguments.of("+7-999-123-45-67 Бумкс", "Спасибо, контакты сохранены."),
                Arguments.of("+7-789-312-33-00 Евлампий", "Спасибо, контакты сохранены."),
                Arguments.of("+7(789)312-33-00 Архонтий", PROBLEM_OCCURS_MSG)
        );
    }

    private static Stream<Arguments> paramsForShelterMenu() {
        return Stream.of(
                Arguments.of(USER_CONTACTS_CMD, USER_CONTACTS_MSG),
                Arguments.of(SAFETY_CMD, SAFETY_MSG),
                Arguments.of(SHELTER_CONTACTS_CMD, SHELTER_CONTACTS_MSG),
                Arguments.of(SHELTER_INFO_CMD, SHELTER_INFO_MSG)
        );
    }

    private static Stream<Arguments> paramsForConsultingMenu() {
        return Stream.of(
                Arguments.of(DECLINE_REASONS_CMD, DECLINE_REASONS_MSG),
                Arguments.of(CATS_CMD, CATS_INFO_MSG),
                Arguments.of(DOGS_CMD, DOGS_INFO_MSG),
                Arguments.of(HOME_PREPARATION_CMD, HOME_PREPARATION_MSG),
                Arguments.of(MEET_DOG_RULES_CMD, MEET_DOG_RULES_MSG),
                Arguments.of(DOCUMENT_LIST_CMD, DOCUMENT_LIST_MSG),
                Arguments.of(FROM_TRAINER_CMD, FROM_TRAINER_MSG),
                Arguments.of(TRANSPORTATION_CMD, TRANSPORTATION_MSG)
        );
    }

    private UserRecord addUser(UserRecord userRecord) {
        if (userRecord != null) {
            userRepository.save(recordMapper.toEntity(userRecord));
        }
        return userRecord;
    }

    private AnimalRecord addAnimal(AnimalRecord animalRecord) {
        if (animalRecord != null) {
            animalRepository.save(recordMapper.toEntity(animalRecord));
        }
        return animalRecord;
    }
}