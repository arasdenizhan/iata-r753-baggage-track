package io.github.arasdenizhan.bts.management.service.kafka;

import io.github.arasdenizhan.bts.notificationcore.model.kafka.BaggageEvent;

public interface KafkaService {
    boolean send(String id, BaggageEvent payload);
}
