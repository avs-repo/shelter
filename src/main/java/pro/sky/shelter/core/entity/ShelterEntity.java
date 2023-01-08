package pro.sky.shelter.core.entity;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * PostgresSQL data base Entity
 *
 * @autor Shikunov Andrey
 */
@Entity
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShelterEntity shelter)) return false;
        return id.equals(shelter.id) && name.equals(shelter.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }
}
