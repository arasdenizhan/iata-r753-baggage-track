package io.github.arasdenizhan.bts.management.service.outbox;

import io.github.arasdenizhan.bts.management.model.entity.Baggage;

public interface OutboxEventService {
    void saveOutboxEvent(Baggage baggage);
}
