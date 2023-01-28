package pro.sky.shelter.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pro.sky.shelter.core.model.AnimalType;

import java.util.Objects;

/**
 * Animal class, for storing information about an animal
 */

@Entity
@Getter
@Setter
@Table(name = "animal")
public class AnimalEntity {

    /**
     * Animal id field
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Animal type field(cat,dog)
     */
    @Column(name = "animal_type")
    private AnimalType animalType;

    /**
     * Animal name field
     */
    @Column(name = "animal_name")
    private String animalName;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private VolunteerEntity volunteerEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnimalEntity that)) return false;
        return Objects.equals(id, that.id) && animalType == that.animalType && Objects.equals(animalName, that.animalName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, animalType, animalName);
    }
}
