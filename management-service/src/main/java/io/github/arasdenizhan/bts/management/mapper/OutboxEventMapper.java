package io.github.arasdenizhan.bts.management.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.arasdenizhan.bts.management.model.entity.Baggage;
import io.github.arasdenizhan.bts.management.model.entity.OutboxEvent;
import io.github.arasdenizhan.bts.notificationcore.model.kafka.BaggageEvent;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class OutboxEventMapper {

    @Autowired
    private Clock clock;

    public OutboxEvent fromBaggage(Baggage baggage) throws JsonProcessingException {
        return OutboxEvent.builder()
                .id(UUID.randomUUID())
                .tag(baggage.getTag())
                .createdAt(LocalDateTime.now(clock))
                .eventType(baggage.getCurrentStatus())
                .isProcessed(false).
                payload(getBaggageEvent(baggage)).build();
    }

    private BaggageEvent getBaggageEvent(Baggage baggage) {
        return BaggageEvent.builder()
                .tag(baggage.getTag())
                .eventType(baggage.getCurrentStatus().name())
                .flightNumber(baggage.getFlightNumber())
                .origin(baggage.getOrigin().name())
                .destination(baggage.getDestination().name())
                .passengerName(baggage.getPassengerName())
                .timestamp(baggage.getCheckInTime())
                .handlingCarrier("TK")
                .build();
    }
}
