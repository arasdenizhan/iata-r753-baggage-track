package io.github.arasdenizhan.bts.management.service.outbox.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.arasdenizhan.bts.management.exception.OutboxEventException;
import io.github.arasdenizhan.bts.management.mapper.OutboxEventMapper;
import io.github.arasdenizhan.bts.management.model.entity.Baggage;
import io.github.arasdenizhan.bts.management.model.entity.OutboxEvent;
import io.github.arasdenizhan.bts.management.repository.OutboxEventRepository;
import io.github.arasdenizhan.bts.management.service.kafka.KafkaService;
import io.github.arasdenizhan.bts.management.service.outbox.OutboxEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxEventServiceImpl implements OutboxEventService {
    private final OutboxEventRepository repository;
    private final OutboxEventMapper outboxEventMapper;
    private final KafkaService kafkaService;
    private final Clock clock;

    @Override
    public void saveOutboxEvent(Baggage baggage) {
        try {
            log.info("OutboxEvent save request for baggageTag={}", baggage.getTag());
            OutboxEvent outboxEvent = outboxEventMapper.fromBaggage(baggage);
            repository.save(outboxEvent);
            log.info("OutboxEvent saved successfully for baggageTag={}", baggage.getTag());
        } catch (JsonProcessingException e) {
            log.error("Error occurred while mapping outbox event!", e);
            throw new OutboxEventException(e.getMessage(), e);
        }
    }

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void processOutboxEvents(){
        List<OutboxEvent> notProcessedEvents = repository.findNotProcessedEvents();
        if(notProcessedEvents != null && !notProcessedEvents.isEmpty()){
            log.info("Found {} amount of not processed outbox events.", notProcessedEvents.size());
            for(OutboxEvent event : notProcessedEvents){
                log.info("OutboxEvent with id={} will be processed.", event.getId());
                boolean isSent = kafkaService.send(event.getId().toString(), event.getPayload());
                if(isSent){
                    event.setProcessed(true);
                    event.setUpdatedAt(LocalDateTime.now(clock));
                    repository.save(event);
                    log.info("OutboxEvent with id={} processed successfully.", event.getId());
                } else {
                    log.warn("OutboxEvent with id={} not processed successfully!", event.getId());
                }
            }
        }
    }
}
