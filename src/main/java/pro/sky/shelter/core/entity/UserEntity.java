package pro.sky.shelter.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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
     * Поле хранит дату первого отчета
     * */
    @Column(name = "date")
    private LocalDateTime date;

    /**
     * Поле для связи с таблицей AnimalEntity
     */
    @OneToOne
    @JoinColumn(name = "animal_id")
    private AnimalEntity animalEntity;

    /**
     * Поле для связи с таблицей ReportEntity
     */
    @OneToMany(mappedBy = "userEntity")
    private List<ReportEntity> reportEntity;

    @Column(name = "isvolunteer")
    private Boolean isVolunteer;

    public UserEntity() {
    }

    public UserEntity(Long chatId) {
        this.chatId = chatId;
    }

    public UserEntity(Long chatId, String userName, String phone, Boolean isVolunteer) {
        this.userName = userName;
        this.chatId = chatId;
        this.phone = phone;
        this.isVolunteer = isVolunteer;
    }

    public AnimalEntity getAnimalEntity() {
        return animalEntity;
    }

    public void setAnimalEntity(AnimalEntity animalEntity) {
        this.animalEntity = animalEntity;
    }

    public List<ReportEntity> getReportEntity() {
        return reportEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(userName, that.userName) && Objects.equals(chatId, that.chatId) && Objects.equals(phone, that.phone) && Objects.equals(date, that.date) && Objects.equals(animalEntity, that.animalEntity) && Objects.equals(reportEntity, that.reportEntity) && Objects.equals(isVolunteer, that.isVolunteer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, chatId, phone, date, animalEntity, reportEntity, isVolunteer);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", chatId=" + chatId +
                ", phone='" + phone + '\'' +
                ", date=" + date +
                ", animalEntity=" + animalEntity +
//                ", reportEntity=" + reportEntity +
                ", isVolunteer=" + isVolunteer +
                '}';
    }
}
