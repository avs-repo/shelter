package pro.sky.shelter.scheduler.dialog;

import pro.sky.shelter.scheduler.dto.DialogDto;

public interface DialogInterface {
    boolean isSupport(DialogDto dialogDto);
    boolean process(DialogDto dialogDto);
    String getMessage();
}
