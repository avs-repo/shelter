package pro.sky.shelter.scheduler.exception;

import static pro.sky.shelter.configuration.BotConstants.ERROR_MSG;

public class IntervalDateIncorrectException extends RuntimeException {
    public IntervalDateIncorrectException() {
        super(ERROR_MSG);
    }
}
