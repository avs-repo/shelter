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

    public BotService(TelegramBot bot, Map<String, DialogInterface> supportedDialogs) {
        this.telegramBot = bot;
        this.supportedDialogs = supportedDialogs;
    }

    /**
     * Gets the Bot update request
     * <p>
     * Checks that message is <b>not null</b> and has <b>text data</b>.
     * Chooses needed dialog and sending response to user.
     */
    public void process(Update update) {
        try {
            for (DialogInterface dialog : supportedDialogs.values()) {
                if (update.message() == null || update.message().text() == null) {
                    return;
                }
                Message incomeMessage = update.message();
                DialogDto dto = new DialogDto(incomeMessage.chat().id(), update.message().from().firstName(), incomeMessage.text());
                if (dialog.isSupport(dto) && dialog.process(dto)) {
                    sendResponse(dto.chatId(), dialog.getMessage(), dialog.getButtons());
                    return;
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

    private ReplyKeyboardMarkup initKeyboard(KeyboardButton[] buttons) {
        //Создаем объект будущей клавиатуры и выставляем нужные настройки
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(buttons);
        keyboardMarkup.resizeKeyboard(true); //подгоняем размер
        keyboardMarkup.oneTimeKeyboard(true); //скрываем после использования

        //добавляем лист кнопок в главный объект
        return keyboardMarkup;
    }
}
