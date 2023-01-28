package pro.sky.shelter.core.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Класс Волонтер, для хранения данных о волонтере
 * */

@Getter
@Setter
@EqualsAndHashCode
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
    @Column(name = "lastname")
    private String lastName;

    @OneToMany(mappedBy = "volunteerEntity")
    private List<AnimalEntity> animalEntity;


    public VolunteerEntity() {
    }
}
