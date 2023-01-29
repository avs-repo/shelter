package pro.sky.shelter.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.shelter.core.entity.AnimalEntity;
import pro.sky.shelter.core.entity.VolunteerEntity;

/**
 * Репозиторий хранения информации о волонтерах
 */
@Repository
public interface VolunteerRepository extends JpaRepository<VolunteerEntity, Long> {
}
