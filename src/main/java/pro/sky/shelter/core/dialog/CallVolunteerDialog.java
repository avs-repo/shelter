package pro.sky.shelter.core.dialog;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.stereotype.Component;
import pro.sky.shelter.core.dto.DialogDto;
import pro.sky.shelter.core.repository.UserRepository;
import pro.sky.shelter.service.UserService;

import static pro.sky.shelter.configuration.BotConstants.*;

/**
 * Dialog - позвать волонтера
 *
 * @autor Shikunov Andrey
 */
@Component
public class CallVolunteerDialog implements DialogInterface {
    private final UserService userService;

    public CallVolunteerDialog(UserService userService) {
        this.userService = userService;
    }

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
        userService.openChat(chatId);
        return VOLUNTEER_MSG;
    }

    @Override
    public ReplyKeyboardMarkup getKeyboard() {
        return CHAT_KEYBOARD;
    }
}
