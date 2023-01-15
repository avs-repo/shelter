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
    @Column(name = "user_name")
    private String userName;
    /**
     * Users chat ID
     */
    @Column(name = "chat_id")
    private Long chatId;
    /**
     * Users phone number
     */
    @Column(name = "phone")
    private String phone;

    /**
     * Поле для связи с таблицей AnimalEntity
     */
    @OneToOne
    @PrimaryKeyJoinColumn
    private AnimalEntity animalEntity;

    /**
     * Поле для связи с таблицей ReportEntity
     */
    @OneToMany
    @PrimaryKeyJoinColumn
    private List<ReportEntity> reportEntity;

}
