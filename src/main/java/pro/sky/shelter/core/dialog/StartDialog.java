package pro.sky.shelter.core.dialog;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import org.springframework.stereotype.Component;
import pro.sky.shelter.core.dto.DialogDto;
import pro.sky.shelter.core.entity.UserEntity;
import pro.sky.shelter.core.repository.UserRepository;

import static pro.sky.shelter.configuration.BotConstants.*;
import static pro.sky.shelter.configuration.BotConstants.DOGS_CMD;

/**
 * Initiation - /start dialog
 *
 * @autor Shikunov Andrey
 */
@Component
public class StartDialog implements DialogInterface {

    private final UserRepository repository;
    private boolean isNewDialog = false;
    private UserEntity entity;

    public StartDialog(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isSupport(DialogDto dialogDto) {
        return dialogDto.message().equals(INITIAL_CMD);
    }

    @Override
    public boolean process(DialogDto dialogDto) {
        entity = getEntity(dialogDto);
        repository.save(entity);
        return true;
    }

    private UserEntity getEntity(DialogDto dialogDto) {
        UserEntity userEntity = repository.getUserEntityByChatId(dialogDto.chatId());
        if (userEntity == null) {
            userEntity = new UserEntity();
            isNewDialog = true;

            userEntity.setChatId(dialogDto.chatId());
            userEntity.setUserName(dialogDto.name());
        }
        return userEntity;
    }

    /**
     * /start - welcome information message
     *
     * @return Dog shelter message as String
     */
    @Override
    public String getMessage() {
        if (isNewDialog) {
            return GREETING_MSG;
        } else {
            return "Здравствуйте " + entity.getUserName() + ".\nЧем можем помочь?";
        }
    }

    @Override
    public KeyboardButton[] getButtons() {
        return new KeyboardButton[]{new KeyboardButton(SHELTER_INFO_CMD), new KeyboardButton(CATS_CMD), new KeyboardButton(DOGS_CMD)};
    }
}
