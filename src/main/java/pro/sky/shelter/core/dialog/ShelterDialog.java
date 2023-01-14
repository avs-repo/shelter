package pro.sky.shelter.core.dialog;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import org.springframework.stereotype.Component;
import pro.sky.shelter.core.dto.DialogDto;

import static pro.sky.shelter.configuration.BotConstants.*;

@Component
public class ShelterDialog implements DialogInterface {
    /**
     * Initiation - /info dialog
     *
     * @autor Shikunov Andrey
     */
    @Override
    public boolean isSupport(DialogDto dialogDto) {
        return dialogDto.message().equals(SHELTER_INFO_CMD);
    }

    @Override
    public boolean process(DialogDto dialogDto) {
        return true;
    }

    /**
     * /info shelter contact information message
     *
     * @return Dog shelter message as String
     */
    @Override
    public String getMessage() {
        return SHELTER_INFO_MSG;
    }

    @Override
    public KeyboardButton[] getButtons() {
        return new KeyboardButton[]{new KeyboardButton(CATS_CMD), new KeyboardButton(DOGS_CMD)};
    }


}
