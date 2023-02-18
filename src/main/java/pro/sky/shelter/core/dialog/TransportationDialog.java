package pro.sky.shelter.core.dialog;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.stereotype.Component;
import pro.sky.shelter.core.dto.DialogDto;

import static pro.sky.shelter.configuration.BotConstants.*;

/**
 * /dog shelter dialog
 *
 * @autor Shikunov Andrey
 */
@Component
public class TransportationDialog implements DialogInterface {
    @Override
    public boolean isSupport(DialogDto dialogDto) {
        return dialogDto.message().equals(TRANSPORTATION_CMD);
    }

    @Override
    public boolean process(DialogDto dialogDto) {
        return true;
    }

    /**
     * Диалог "Что нужно для транспортировки"
     *
     * @return Сообщение пользователю, как String
     */
    @Override
    public String getMessage(Long chatId) {
        return TRANSPORTATION_MSG;
    }

    @Override
    public ReplyKeyboardMarkup getKeyboard() {
        return CONSULTING_KEYBOARD;
    }
}
