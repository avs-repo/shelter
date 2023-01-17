package pro.sky.shelter.core.dialog;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import org.springframework.stereotype.Component;
import pro.sky.shelter.core.dto.DialogDto;

import static pro.sky.shelter.configuration.BotConstants.*;

/**
 * /cat shelter dialog
 *
 * @autor Shikunov Andrey
 */
@Component
public class CatsDialog implements DialogInterface {
    @Override
    public boolean isSupport(DialogDto dialogDto) {
        return dialogDto.message().equals(CATS_CMD);
    }

    @Override
    public boolean process(DialogDto dialogDto) {
        return true;
    }

    /**
     * /cat shelter information message
     *
     * @return Cat shelter message as String
     */
    @Override
    public String getMessage() {
        return CATS_INFO_MSG;
    }

    @Override
    public KeyboardButton[] getButtons() {
        return KEYBOARD_ALL_BUTTONS;
    }
}
