package pro.sky.shelter.core.record;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс хранения информации о фото животного
 */
@Getter
@Setter
public class AnimalPhotoRecord {
    private Long id;
    private String mediaType;
    public AnimalPhotoRecord(Long id, String mediaType) {
        this.id = id;
        this.mediaType = mediaType;
    }
}
