package pro.sky.shelter.core.dialog;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import org.springframework.stereotype.Component;
import pro.sky.shelter.core.dto.DialogDto;

import static pro.sky.shelter.configuration.BotConstants.*;

/**
 * /dog shelter dialog
 *
 * @autor Shikunov Andrey
 */
@Component
public class DogsDialog implements DialogInterface {
    @Override
    public boolean isSupport(DialogDto dialogDto) {
        return dialogDto.message().equals(DOGS_CMD);
    }

    @Override
    public boolean process(DialogDto dialogDto) {
        return true;
    }

    /**
     * /dog shelter information message
     *
     * @return Dog shelter message as String
     */
    @Override
    public String getMessage() {
        return DOGS_INFO_MSG;
    }

    @Override
    public KeyboardButton[] getButtons() {
        return new KeyboardButton[]{new KeyboardButton(CATS_CMD)};
    }
}
