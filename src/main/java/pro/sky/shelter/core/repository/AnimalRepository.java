package pro.sky.shelter.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.shelter.core.entity.AnimalEntity;

/**
 * Репозиторий хранения информации о животных
 */
@Repository
public interface AnimalRepository extends JpaRepository<AnimalEntity, Long> {
}
