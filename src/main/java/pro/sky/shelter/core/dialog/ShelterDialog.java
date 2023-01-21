package pro.sky.shelter.core.dialog;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.stereotype.Component;
import pro.sky.shelter.core.dto.DialogDto;

import static pro.sky.shelter.configuration.BotConstants.*;

/**
 * Dialog - информация о приюте
 *
 * @autor Shikunov Andrey
 */
@Component
public class ShelterDialog implements DialogInterface {

    @Override
    public boolean isSupport(DialogDto dialogDto) {
        return dialogDto.message().equals(SHELTER_INFO_CMD);
    }

    @Override
    public boolean process(DialogDto dialogDto) {
        return true;
    }

    /**
     * shelter main information message
     *
     * @return Dog shelter message as String
     */
    @Override
    public String getMessage(Long chatId) {
        return SHELTER_INFO_MSG;
    }
    @Override
    public ReplyKeyboardMarkup getKeyboard() {
        return SHELTER_KEYBOARD;
    }
}
