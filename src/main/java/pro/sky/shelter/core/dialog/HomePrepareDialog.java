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
public class HomePrepareDialog implements DialogInterface {
    @Override
    public boolean isSupport(DialogDto dialogDto) {
        return dialogDto.message().equals(HOME_PREPARATION_CMD);
    }

    @Override
    public boolean process(DialogDto dialogDto) {
        return true;
    }

    /**
     * Диалог "как подготовить дом"
     *
     * @return Сообщение пользователю, как String
     */
    @Override
    public String getMessage(Long chatId) {
        return HOME_PREPARATION_MSG;
    }

    @Override
    public ReplyKeyboardMarkup getKeyboard() {
        return CONSULTING_KEYBOARD;
    }
}
