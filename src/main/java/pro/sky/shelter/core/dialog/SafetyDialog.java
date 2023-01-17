package pro.sky.shelter.core.dialog;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import org.springframework.stereotype.Component;
import pro.sky.shelter.core.dto.DialogDto;

import static pro.sky.shelter.configuration.BotConstants.*;

@Component
public class SafetyDialog implements DialogInterface {
    @Override
    public boolean isSupport(DialogDto dialogDto) {
        return dialogDto.message().equals(SAFETY_CMD);
    }

    @Override
    public boolean process(DialogDto dialogDto) {
        return true;
    }

    /**
     * Safety information message
     *
     * @return Shelter's safety rules message as String
     */
    @Override
    public String getMessage() {
        return SAFETY_MSG;
    }

    @Override
    public KeyboardButton[] getButtons() {
        return KEYBOARD_ALL_BUTTONS;
    }
}
