package pro.sky.shelter.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * PostgresSQL data base Entity
 *
 * @autor Shikunov Andrey
 */
@Entity
@Getter
@Setter
@Table(name = "shelters")

public class ShelterEntity {
    /**
     * Generated unique ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Shelter name
     */
    private String name;
    /**
     * Shelters address
     */
    private String address;
    /**
     * Shelters opening hours
     */
    private String openingHours;

}
