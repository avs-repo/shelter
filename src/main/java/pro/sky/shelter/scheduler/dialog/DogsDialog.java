package pro.sky.shelter.scheduler.dialog;

import org.springframework.stereotype.Component;
import pro.sky.shelter.scheduler.dto.DialogDto;

import static pro.sky.shelter.configuration.BotConstants.*;

@Component
public class DogsDialog implements DialogInterface {
    @Override
    public boolean isSupport(DialogDto dialogDto) {
        return dialogDto.message().equals(DOGS_MSG);
    }

    @Override
    public boolean process(DialogDto dialogDto) {
        return true;
    }

    @Override
    public String getMessage() {
        return DOGS_INFO_MSG;
    }
}
