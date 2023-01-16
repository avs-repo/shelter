package pro.sky.shelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import pro.sky.shelter.core.dialog.DialogInterface;
import pro.sky.shelter.core.dto.DialogDto;
import pro.sky.shelter.core.exception.IntervalDateIncorrectException;

import java.io.IOException;
import java.util.Map;

/**
 * Class-service for TelegramBot
 *
 * @autor Shikunov Andrey
 */
@Service
public class BotService {

    /**
     * TelegramBot instance that this BotService is responsible for
     */
    private final TelegramBot telegramBot;


    private final Logger logger = LoggerFactory.getLogger(BotService.class);

    /**
     * Map of supported dialogs
     */
    private final Map<String, DialogInterface> supportedDialogs;
    private final ContentSaverService contentSaverService;

    public BotService(TelegramBot bot, Map<String, DialogInterface> supportedDialogs, ContentSaverService contentSaverService) {
        this.telegramBot = bot;
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
        if (update == null) {
            logger.debug("Method processUpdate detected null update");
            return;
        }
        try {
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
                    DialogDto dto = new DialogDto(incomeMessage.chat().id(), update.message().from().firstName(), incomeMessage.text());
                    if (dialog.isSupport(dto) && dialog.process(dto)) {
                        sendResponse(dto.chatId(), dialog.getMessage(), dialog.getButtons());
                        return;
                    } else {
                        logger.debug("ChatId={}; Detected null text in message", incomeMessage.chat().id());
                    }
                }
            }
        } catch (IntervalDateIncorrectException exception) {
            sendResponse(update.message().chat().id(), exception.getMessage(), null);
        }
    }

    /**
     * Gets the Telegram chatId and incoming message
     * <p>
     * Executes the message send to user.
     */
    public void sendResponse(Long chatId, String message, KeyboardButton[] buttons) {
        SendMessage preparedMessage = new SendMessage(chatId, message);
        if (buttons != null) preparedMessage.replyMarkup(initKeyboard(buttons));
        SendResponse response = telegramBot.execute(preparedMessage);
        if (response == null) {
            logger.debug("ChatId={}; Method sendMessage did not receive a response", chatId);
        } else if (response.isOk()) {
            logger.debug("ChatId={}; Method sendMessage has completed sending the message", chatId);
        } else {
            logger.debug("ChatId={}; Method sendMessage received an error : {}", chatId, response.errorCode());
        }
    }

    /**
     * Gets the needed buttons for dialog and implements keyboard to response
     */
    private ReplyKeyboardMarkup initKeyboard(KeyboardButton[] buttons) {
        //Создаем объект будущей клавиатуры и выставляем нужные настройки
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(buttons);
        keyboardMarkup.resizeKeyboard(true); //подгоняем размер
        keyboardMarkup.oneTimeKeyboard(true); //скрываем после использования

        //добавляем лист кнопок в главный объект
        return keyboardMarkup;
    }
}
