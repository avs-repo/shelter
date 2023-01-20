package pro.sky.shelter.core.dialog;

import org.springframework.stereotype.Component;
import pro.sky.shelter.core.dto.DialogDto;

import static pro.sky.shelter.configuration.BotConstants.*;

/**
 * Dialog - позвать волонтера
 *
 * @autor Shikunov Andrey
 */
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
    public String getMessage(Long chatId) {
        return VOLUNTEER_MSG;
    }
}
