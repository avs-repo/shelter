package pro.sky.shelter.service;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.inject.Singleton;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import pro.sky.shelter.core.dialog.DialogInterface;
import pro.sky.shelter.core.dialog.StartDialog;
import pro.sky.shelter.core.repository.ReportRepository;
import pro.sky.shelter.core.repository.UserRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static pro.sky.shelter.configuration.BotConstants.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BotServiceTest {

    @MockBean
    private TelegramBot telegramBot;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ContentSaverService photoOfAnimalService;

    @MockBean
    private ReportRepository reportRepository;
//    @MockBean
//    private Map<String, DialogInterface> supportedDialogs;
@MockBean
@InjectMocks
private BotService out;

//    @InjectMocks
//    private BotService out;

    @Test
   // @MethodSource("paramsForWelcomeMenu")
    public void welcomeMenuTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(Objects.requireNonNull(BotServiceTest.class.getResource("message.json")).toURI()));
        Update update = getUpdate(json, INITIAL_CMD);

        out.process(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        assertThat(actual.getParameters().get("reply_markup")).isNotNull();
        assertThat(actual.getParameters().get("text")).isEqualTo(GREETING_MSG);
    }

    private Update getUpdate(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%command%", replaced), Update.class);
    }

    public static Stream<Arguments> paramsForWelcomeMenu() {
        return Stream.of(
                Arguments.of(INITIAL_CMD),
                Arguments.of(GO_BACK_CMD)
        );
    }
}