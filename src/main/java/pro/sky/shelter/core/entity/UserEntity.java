package pro.sky.shelter.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "users")
public class UserEntity {
    /**
     * Generated unique user ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * User name
     */
    private String userName;
    /**
     * Users chad ID
     */
    private Long chatId;
    /**
     * Users phone number
     */
    private String phone;

    /**
     * Поле для связи с таблицей AnimalEntity
     */
    @OneToOne
    private AnimalEntity animalEntity;

    /**
     * Поле для связи с таблицей ReportEntity
     */
    @OneToMany
    private List<ReportEntity> reportEntity;

}
