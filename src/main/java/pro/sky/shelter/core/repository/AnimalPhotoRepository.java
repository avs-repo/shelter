package pro.sky.shelter.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.shelter.core.entity.AnimalPhotoEntity;

/**
 * Репозиторий хранения фотографий животных
 */
@Repository
public interface AnimalPhotoRepository extends JpaRepository<AnimalPhotoEntity, Long> {
}
