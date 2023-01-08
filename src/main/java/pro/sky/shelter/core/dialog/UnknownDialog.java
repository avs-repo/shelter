package pro.sky.shelter.core.dialog;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pro.sky.shelter.core.dto.DialogDto;

import static pro.sky.shelter.configuration.BotConstants.PROBLEM_OCCURS_MSG;

@Component
@Order(-1)
public class UnknownDialog implements DialogInterface {
    @Override
    public boolean isSupport(DialogDto dialogDto) {
        return true;
    }

    @Override
    public boolean process(DialogDto dialogDto) {
        return true;
    }

    @Override
    public String getMessage() {
        return PROBLEM_OCCURS_MSG;
    }
}
