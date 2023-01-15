package pro.sky.shelter.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pro.sky.shelter.core.model.AnimalType;

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

}
