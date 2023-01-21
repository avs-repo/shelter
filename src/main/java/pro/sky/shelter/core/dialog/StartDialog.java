package pro.sky.shelter.core.dialog;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.stereotype.Component;
import pro.sky.shelter.core.dto.DialogDto;
import pro.sky.shelter.core.record.UserRecord;
import pro.sky.shelter.service.UserService;

import static pro.sky.shelter.configuration.BotConstants.*;

/**
 * Initiation - /start dialog
 *
 * @autor Shikunov Andrey
 */
@Component
public class StartDialog implements DialogInterface {

    private final UserService userService;
    private DialogDto dialog;

    public StartDialog(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isSupport(DialogDto dialogDto) {
        return dialogDto.message().equals(INITIAL_CMD) || dialogDto.message().equals(GO_BACK_CMD);
    }

    @Override
    public boolean process(DialogDto dialogDto) {
        dialog = dialogDto;
        return true;
    }

    /**
     * /start - welcome information message
     *
     * @return Welcome message as String
     */
    @Override
    public String getMessage(Long chatId) {
        UserRecord userRecord = userService.findUserByChatId(chatId);
        if (userRecord == null) {
            userService.createUser(dialog);
            return GREETING_MSG;
        } else {
            return "Здравствуйте " + userRecord.getUserName() + "!\nЧем можем помочь?";
        }
    }

    @Override
    public ReplyKeyboardMarkup getKeyboard() {
        return WELCOME_KEYBOARD;
    }
}
