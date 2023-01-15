package pro.sky.shelter.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
}
