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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShelterEntity that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(openingHours, that.openingHours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, openingHours);
    }

    @Override
    public String toString() {
        return "ShelterEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", openingHours='" + openingHours + '\'' +
                '}';
    }
}
