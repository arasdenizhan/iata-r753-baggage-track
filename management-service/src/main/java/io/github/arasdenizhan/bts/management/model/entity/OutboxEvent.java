package io.github.arasdenizhan.bts.management.model.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import io.github.arasdenizhan.bts.management.model.enums.EventType;
import io.github.arasdenizhan.bts.notificationcore.model.kafka.BaggageEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String tag;

    @Column(nullable = false)
    private EventType eventType;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private BaggageEvent payload;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean isProcessed;
}
