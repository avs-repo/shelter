package pro.sky.shelter.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "animal_photo")
public class AnimalPhotoEntity {

    /**
     * Поле ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Поле путь к файлу
     */
    @Column(name = "file_path")
    private String filePath;

    /**
     * Поле размер файла
     */
    @Column(name = "file_size")
    private long fileSize;

    /**
     * Поле тип файла
     */
    @Column(name = "media_type")
    private String mediaType;

    /**
     * Поле информация о файле
     */
    @Column(name = "data")
    private byte[] data;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnimalPhotoEntity that)) return false;
        return fileSize == that.fileSize && Objects.equals(id, that.id) && Objects.equals(filePath, that.filePath) && Objects.equals(mediaType, that.mediaType) && Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, filePath, fileSize, mediaType);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
