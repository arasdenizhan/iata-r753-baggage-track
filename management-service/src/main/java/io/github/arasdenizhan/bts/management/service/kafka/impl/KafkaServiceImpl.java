package io.github.arasdenizhan.bts.management.service.kafka.impl;

import io.github.arasdenizhan.bts.notificationcore.model.kafka.BaggageEvent;
import io.github.arasdenizhan.bts.management.service.kafka.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {
    private static final String BAGGAGE_TOPIC = "baggage.tracking";
    private final KafkaTemplate<String, BaggageEvent> template;

    @Override
    public boolean send(String id, BaggageEvent payload) {
        try {
            log.info("KafkaService will send event id={} to {} topic.", id, BAGGAGE_TOPIC);
            template.send(BAGGAGE_TOPIC, payload.getTag(), payload);
            log.info("KafkaService successfully send event id={} to {} topic.", id, BAGGAGE_TOPIC);
            return true;
        } catch (Exception e) {
            log.error("KafkaService has error while converting payload to string!", e);
            log.info("Skipping send operation for event id={}", id);
            return false;
        }
    }
}
