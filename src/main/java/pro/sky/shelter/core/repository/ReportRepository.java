package pro.sky.shelter.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.shelter.core.entity.ReportEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий хранения отчетов от владельцев о состоянии животных
 */
@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
    List<ReportEntity> findByDate(LocalDateTime localDateTime);
}
