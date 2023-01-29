package pro.sky.shelter.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

/**
 * Класс Волонтер, для хранения данных о волонтере
 * */

@Getter
@Setter
@ToString

@Table(name = "volunteer")

@Entity
public class VolunteerEntity {

    /**
     * поле id
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * id чата
     * */
    @Column(name = "chat_id")
    private Long chatId;
    /**
     * имя волонтёра
     * */
    @Column(name = "name")
    private String name;
    /**
     * фамилия волонтёра
     * */
    @Column(name = "last_name")
    private String lastName;

    @OneToMany(mappedBy = "volunteerEntity")
    @ToString.Exclude
    private List<AnimalEntity> animalEntity;

    public VolunteerEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VolunteerEntity that = (VolunteerEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
