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
public class GetReportDialog implements DialogInterface {
    @Override
    public boolean isSupport(DialogDto dialogDto) {
        return dialogDto.message().equals(SEND_REPORT_CMD);
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
    public String getMessage(Long chatId) {
        return SEND_REPORT_MSG;
    }

    @Override
    public ReplyKeyboardMarkup getKeyboard() {
        return WELCOME_KEYBOARD;
    }
}
