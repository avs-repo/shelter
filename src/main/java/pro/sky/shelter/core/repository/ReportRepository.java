package pro.sky.shelter.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.shelter.core.entity.ReportEntity;

/**
 * Репозиторий хранения отчетов от владельцев о состоянии животных
 */
@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
}
