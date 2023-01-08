package pro.sky.shelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
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
                DialogDto dto = new DialogDto(incomeMessage.chat().id(), incomeMessage.text());
                if (dialog.isSupport(dto) && dialog.process(dto)) {
                    sendResponse(dto.chatId(), dialog.getMessage());
                    return;
                }
            }
        } catch (IntervalDateIncorrectException exception) {
            sendResponse(update.message().chat().id(), exception.getMessage());
        }
    }

    /**
     * Gets the Telegram chatId and incoming message
     * <p>
     * Executes the message send to user.
     */
    public void sendResponse(Long chatId, String message) {
        SendMessage preparedMessage = new SendMessage(chatId, message);
        telegramBot.execute(preparedMessage);
    }
}
