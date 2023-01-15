package pro.sky.shelter.core.dialog;

import com.pengrad.telegrambot.model.request.KeyboardButton;
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
    public String getMessage() {
        return PROBLEM_OCCURS_MSG;
    }

    @Override
    public KeyboardButton[] getButtons() {
        return new KeyboardButton[]{new KeyboardButton(GREETING_MSG)};
    }
}
