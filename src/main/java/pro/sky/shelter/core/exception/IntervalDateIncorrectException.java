package pro.sky.shelter.core.exception;

import static pro.sky.shelter.configuration.BotConstants.ERROR_MSG;

/**
 * Custom Exception
 *
 * @autor Shikunov Andrey
 */
public class IntervalDateIncorrectException extends RuntimeException {
    public IntervalDateIncorrectException() {
        super(ERROR_MSG);
    }
}
