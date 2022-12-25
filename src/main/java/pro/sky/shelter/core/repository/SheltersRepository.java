package pro.sky.shelter.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.shelter.core.entity.ShelterEntity;
/**
 * JPA repository
 *
 * @autor Shikunov Andrey
 */
@Repository
public interface SheltersRepository extends JpaRepository<ShelterEntity, Long> {
}
