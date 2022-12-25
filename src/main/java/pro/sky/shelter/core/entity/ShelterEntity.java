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
     * Information about shelter
     */
    private String info;

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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
