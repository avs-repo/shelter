package pro.sky.shelter.core.dialog;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import org.springframework.stereotype.Component;
import pro.sky.shelter.core.dto.DialogDto;

import static pro.sky.shelter.configuration.BotConstants.*;

@Component
public class CallVolunteerDialog implements DialogInterface {

    @Override
    public boolean isSupport(DialogDto dialogDto) {
        return dialogDto.message().equals(VOLUNTEER_CMD);
    }

    @Override
    public boolean process(DialogDto dialogDto) {
        return true;
    }

    /**
     * Answer to user: Please wait for volunteer message
     *
     * @return volunteer message as String
     */
    @Override
    public String getMessage() {
        return VOLUNTEER_MSG;
    }

    @Override
    public KeyboardButton[] getButtons() {
        return KEYBOARD_ALL_BUTTONS;
    }
}
