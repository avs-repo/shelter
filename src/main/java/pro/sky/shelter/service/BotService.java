package pro.sky.shelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import pro.sky.shelter.core.dialog.DialogInterface;
import pro.sky.shelter.core.dto.DialogDto;
import pro.sky.shelter.core.entity.UserEntity;
import pro.sky.shelter.core.repository.UserRepository;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pro.sky.shelter.configuration.BotConstants.KEYBOARD_FOR_USER;

/**
 * Main service for TelegramBot
 *
 * @autor Shikunov Andrey
 */
@Service
public class BotService {

    /**
     * TelegramBot instance that this BotService is responsible for
     */
    private final TelegramBot telegramBot;

    private final String parsePhone = "([+][7]-\\d{3}-\\d{3}-\\d{2}-\\d{2})(\\s)([\\W+]+)";
    private final UserRepository userRepository;
    private final AnimalService animalService;
    private final Logger logger = LoggerFactory.getLogger(BotService.class);

    /**
     * Map of supported dialogs
     */
    private final Map<String, DialogInterface> supportedDialogs;
    private final ContentSaverService contentSaverService;

    public BotService(TelegramBot bot, UserRepository userRepository, AnimalService animalService, Map<String, DialogInterface> supportedDialogs, ContentSaverService contentSaverService) {
        this.telegramBot = bot;
        this.userRepository = userRepository;
        this.animalService = animalService;
        this.supportedDialogs = supportedDialogs;
        this.contentSaverService = contentSaverService;
    }

    /**
     * Gets the Bot update request
     * <p>
     * Checks is there a photo in message, if true - proceed with photo
     * Checks that message is <b>not null</b> and has <b>text data</b> - proceed with text.
     * Chooses needed dialog and sending response to user.
     */
    public void process(Update update) {
        if (update == null || update.message().document() != null) {
            logger.debug("Method processUpdate detected document or null update");
            return;
        }
        for (DialogInterface dialog : supportedDialogs.values()) {
            Message incomeMessage = update.message();
            if (update.message() == null) {
                logger.debug("ChatId={}; Detected null message in update", incomeMessage.chat().id());
                return;
            }
            if (incomeMessage.photo() != null) {
                int maxPhotoIndex = update.message().photo().length - 1;
                if (update.message().photo()[maxPhotoIndex].fileId() != null) {
                    try {
                        contentSaverService.savePhoto(update);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                } else {
                    logger.debug("ChatId={}; Detected null fileId in photo", incomeMessage.chat().id());
                }
            } else if (incomeMessage.text() != null) {
                if (incomeMessage.text().matches(parsePhone)) {
                    logger.info("ChatId={}; Parsing phone number", incomeMessage.chat().id());
                    parsing(incomeMessage.text(), incomeMessage.chat().id());
                    return;
                }
                DialogDto dto = new DialogDto(incomeMessage.chat().id(), update.message().from().firstName(), incomeMessage.text());
                if (dialog.isSupport(dto) && dialog.process(dto)) {
                    sendResponse(dto.chatId(), dialog.getMessage(dto.chatId()), true);
                    return;
                } else {
                    logger.debug("ChatId={}; Detected null text in message", incomeMessage.chat().id());
                }
            }
        }
    }

    /**
     * Gets the Telegram chatId, incoming message и enable keyboard
     * <p>
     * Executes the message send to user.
     */
    public void sendResponse(Long chatId, String message, boolean enableKeyboard) {
        SendMessage preparedMessage = new SendMessage(chatId, message);
        if (enableKeyboard) preparedMessage.replyMarkup(KEYBOARD_FOR_USER);
        telegramBot.execute(preparedMessage);
    }

    public void parsing(String text, Long chatId) {
        Pattern pattern = Pattern.compile(parsePhone);
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            String phone = matcher.group(1);
            String name = matcher.group(3);
            UserEntity userEntity = userRepository.getUserEntitiesByChatId(chatId);
            if (userEntity == null) {
                userEntity = new UserEntity(chatId, name, phone);
            } else {
                userEntity.setUserName(name);
                userEntity.setPhone(phone);
            }
            userRepository.save(userEntity);
            logger.info("Контакты сохранены.");
            sendResponse(chatId, "Спасибо, контакты сохранены.", true);
        }
    }
}
