package pro.sky.shelter.core.dto;

import java.util.Objects;

/**
 * Data transfer object record
 *
 * @autor Shikunov Andrey
 */
public record DialogDto(Long chatId, String name, String message) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DialogDto dialogDto)) return false;
        return Objects.equals(chatId, dialogDto.chatId) && Objects.equals(message, dialogDto.message);
    }
}
