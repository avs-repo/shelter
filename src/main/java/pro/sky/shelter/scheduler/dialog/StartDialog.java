package pro.sky.shelter.scheduler.dialog;

import org.springframework.stereotype.Component;
import pro.sky.shelter.scheduler.dto.DialogDto;

import static pro.sky.shelter.configuration.BotConstants.GREETING_MSG;
import static pro.sky.shelter.configuration.BotConstants.INITIAL_MSG;

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

    @Override
    public String getMessage() {
        return GREETING_MSG;
    }
}
