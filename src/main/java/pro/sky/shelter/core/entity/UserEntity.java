package pro.sky.shelter.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

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
    @JoinColumn(name = "id")
    private AnimalEntity animalEntity;

    /**
     * Поле для связи с таблицей ReportEntity
     */
    @OneToMany
    @JoinColumn(name = "id")
    private List<ReportEntity> reportEntity;

    public UserEntity() {

    }
    public UserEntity(Long chatId, String userName, String phone) {
        this.userName = userName;
        this.chatId = chatId;
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(userName, that.userName) && Objects.equals(chatId, that.chatId) && Objects.equals(phone, that.phone) && Objects.equals(animalEntity, that.animalEntity) && Objects.equals(reportEntity, that.reportEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, chatId, phone, animalEntity, reportEntity);
    }
}
