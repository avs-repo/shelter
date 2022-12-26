package pro.sky.shelter.core.dialog;

import org.springframework.stereotype.Component;
import pro.sky.shelter.core.dto.DialogDto;

import static pro.sky.shelter.configuration.BotConstants.GREETING_MSG;
import static pro.sky.shelter.configuration.BotConstants.INITIAL_MSG;
/**
 * Initiation - /start dialog
 *
 * @autor Shikunov Andrey
 */
@Component
public class StartDialog implements DialogInterface {
    @Override
    public boolean isSupport(DialogDto dialogDto) {
        return dialogDto.message().equals(INITIAL_MSG);
    }

    @Override
    public boolean process(DialogDto dialogDto) {
        return true;
    }
    /**
     * /start - welcome information message
     *
     * @return Dog shelter message as String
     */
    @Override
    public String getMessage() {
        return GREETING_MSG;
    }
}
