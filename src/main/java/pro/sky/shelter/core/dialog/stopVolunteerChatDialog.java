package pro.sky.shelter.core.dialog;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.stereotype.Component;
import pro.sky.shelter.core.dto.DialogDto;
import pro.sky.shelter.service.UserService;

import static pro.sky.shelter.configuration.BotConstants.*;

/**
 * Dialog - закрытия чата с волонтером
 *
 * @autor Shikunov Andrey
 */
@Component
public class stopVolunteerChatDialog implements DialogInterface {
    private final UserService userService;

    public stopVolunteerChatDialog(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isSupport(DialogDto dialogDto) {
        return dialogDto.message().equals(VOLUNTEER_CHAT_CLOSE);
    }

    @Override
    public boolean process(DialogDto dialogDto) {
        return true;
    }

    /**
     * Закрывает чат с волонтером
     *
     * @return сообщение пользователю
     */
    @Override
    public String getMessage(Long chatId) {
        userService.closeChat(chatId);
        return CLOSE_CHAT_MSG;
    }

    @Override
    public ReplyKeyboardMarkup getKeyboard() {
        return WELCOME_KEYBOARD;
    }
}
