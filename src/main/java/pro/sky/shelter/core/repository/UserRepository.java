package pro.sky.shelter.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.shelter.core.entity.AnimalEntity;
import pro.sky.shelter.core.entity.UserEntity;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий хранения информации о пользователях
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByAnimalEntity(AnimalEntity animalEntity);

    Optional<UserEntity> findUserEntityByChatId(Long chatId);

    UserEntity getUserEntitiesByChatId(Long chatId);

    List<UserEntity> findUserEntitiesByIsVolunteer(boolean isVolunteer);
}
