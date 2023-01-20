package pro.sky.shelter.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.shelter.core.entity.AnimalEntity;

/**
 * Репозиторий хранения информации о животных
 */
public interface AnimalRepository extends JpaRepository<AnimalEntity, Long> {

}
