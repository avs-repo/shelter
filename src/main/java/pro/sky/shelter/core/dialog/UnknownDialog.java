package pro.sky.shelter.core.dialog;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pro.sky.shelter.core.dto.DialogDto;

import static pro.sky.shelter.configuration.BotConstants.*;

@Component
@Order(-1)
public class UnknownDialog implements DialogInterface {
    @Override
    public boolean isSupport(DialogDto dialogDto) {
        return true;
    }

    @Override
    public boolean process(DialogDto dialogDto) {
        return true;
    }

    @Override
    public String getMessage(Long chatId) {
        return PROBLEM_OCCURS_MSG;
    }
    @Override
    public ReplyKeyboardMarkup getKeyboard() {
        return WELCOME_KEYBOARD;
    }
}
