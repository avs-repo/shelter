package pro.sky.shelter.scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.shelter.scheduler.entity.ShelterEntity;

@Repository
public interface SheltersRepository extends JpaRepository<ShelterEntity, Long> {
}
