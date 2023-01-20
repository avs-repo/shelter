package pro.sky.shelter.core.dialog;

import org.springframework.stereotype.Component;
import pro.sky.shelter.core.dto.DialogDto;

import static pro.sky.shelter.configuration.BotConstants.*;

/**
 * Dialog - информация о мерах безопасности
 *
 * @autor Shikunov Andrey
 */
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
    public String getMessage(Long chatId) {
        return SAFETY_MSG;
    }
}
