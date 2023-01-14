package pro.sky.shelter.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.service.spi.InjectService;

@Entity

@Getter
@Setter

@Table(name = "animalPhoto")
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
    private String filePath;

    /**
     * Поле размер файла
     */
    private long fileSize;

    /**
     * Поле тип файла
     */
    private String mediaType;

    /**
     * Поле информация о файле
     */
    private byte[] data;
}
