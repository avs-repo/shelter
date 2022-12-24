package pro.sky.shelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pro.sky.shelter.scheduler.dialog.DialogInterface;
import pro.sky.shelter.scheduler.dto.DialogDto;
import pro.sky.shelter.scheduler.exception.IntervalDateIncorrectException;
import pro.sky.shelter.scheduler.repository.SheltersRepository;

import java.util.Map;

@Service
public class BotService {
    private final TelegramBot telegramBot;
    private final Map<String, DialogInterface> supportedDialogs;

    @Autowired
    private SheltersRepository sheltersRepository;

    public BotService(TelegramBot bot, Map<String, DialogInterface> supportedDialogs) {
        this.telegramBot = bot;
        this.supportedDialogs = supportedDialogs;
    }

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

    public void sendResponse(Long chatId, String message) {
        SendMessage preparedMessage = new SendMessage(chatId, message);
        telegramBot.execute(preparedMessage);
    }
}
