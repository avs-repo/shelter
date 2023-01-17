package pro.sky.shelter.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity

@Getter
@Setter

@Table(name = "report")

public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата создания отчёта
     */
    @Column(name = "date")
    private LocalDateTime date;

    /**
     * Поле для хранения имени животного
     */
    @Column(name = "name")
    private String animalName;

    /**
     * Поле для хранения данных о рационе питания животного
     */
    @Column(name = "diet")
    private String diet;

    /**
     * Поле для хранения данных о состоянии животного
     */
    @Column(name = "health")
    private String health;

    /**
     * Поле, которое описывает изменения в поведении у животного
     */
    @Column(name = "behavior")
    private String behavior;

    /**
     * Поле для связи с таблицей AnimalPhotoEntity
     */
    @OneToOne
    @JoinColumn(name = "animal_photo_id")
    private AnimalPhotoEntity animalPhotoEntity;

    /**
     * Поле для связи с таблицей UserEntity
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;


}