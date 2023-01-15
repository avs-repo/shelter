package pro.sky.shelter.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @Column(name = "name")
    private String name;
    /**
     * Shelters address
     */
    @Column(name = "address")
    private String address;
    /**
     * Shelters opening hours
     */
    @Column(name = "opening_hours")
    private String openingHours;

}
