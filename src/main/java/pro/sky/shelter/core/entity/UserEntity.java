package pro.sky.shelter.core.entity;

import jakarta.persistence.*;

@Entity
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
