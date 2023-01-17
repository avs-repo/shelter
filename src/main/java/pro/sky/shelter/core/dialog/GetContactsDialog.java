package pro.sky.shelter.core.dialog;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import org.springframework.stereotype.Component;
import pro.sky.shelter.core.dto.DialogDto;

import static pro.sky.shelter.configuration.BotConstants.*;

@Component
public class GetContactsDialog implements DialogInterface {
    @Override
    public boolean isSupport(DialogDto dialogDto) {
        return dialogDto.message().equals(USER_CONTACTS_CMD);
    }

    @Override
    public boolean process(DialogDto dialogDto) {
        return true;
    }

    /**
     * Get user contact information
     *
     * @return Get user contacts message as String
     */
    @Override
    public String getMessage() {
        return USER_CONTACTS_MSG;
    }

    @Override
    public KeyboardButton[] getButtons() {
        return KEYBOARD_ALL_BUTTONS;
    }
}
