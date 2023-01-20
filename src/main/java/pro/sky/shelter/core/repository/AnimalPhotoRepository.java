package pro.sky.shelter.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.shelter.core.entity.AnimalPhotoEntity;

/**
 * Репозиторий хранения фотографий животных
 */
public interface AnimalPhotoRepository extends JpaRepository<AnimalPhotoEntity, Long> {
}
