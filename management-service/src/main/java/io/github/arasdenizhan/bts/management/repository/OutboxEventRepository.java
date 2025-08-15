package io.github.arasdenizhan.bts.management.repository;

import io.github.arasdenizhan.bts.management.model.entity.OutboxEvent;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {
   @Query("Select o from OutboxEvent o where o.isProcessed = false")
   List<OutboxEvent> findNotProcessedEvents();
}
