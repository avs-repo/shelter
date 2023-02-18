package pro.sky.shelter.core.dialog;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import pro.sky.shelter.core.dto.DialogDto;

/**
 * Interface of Bot-User dialogs
 *
 * @autor Shikunov Andrey
 */
public interface DialogInterface {
    /**
     * Checks that user message is supported
     *
     * @param dialogDto - users message
     */
    boolean isSupport(DialogDto dialogDto);

    /**
     * Activates the dialog
     *
     * @param dialogDto - users message
     * @return true - if dialog will be processed
     */
    boolean process(DialogDto dialogDto);

    /**
     * Gets the message for respond
     *
     * @return Message as String
     */
    String getMessage(Long chatId);

    /**
     * Which keyboard use
     * @return ReplyKeyboardMarkup to use;
     */
    ReplyKeyboardMarkup getKeyboard();
}
