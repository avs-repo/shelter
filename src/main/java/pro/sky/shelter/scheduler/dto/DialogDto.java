package pro.sky.shelter.scheduler.dto;

import java.util.Objects;

public record DialogDto(Long chatId, String message) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DialogDto dialogDto)) return false;
        return Objects.equals(chatId, dialogDto.chatId) && Objects.equals(message, dialogDto.message);
    }

}
