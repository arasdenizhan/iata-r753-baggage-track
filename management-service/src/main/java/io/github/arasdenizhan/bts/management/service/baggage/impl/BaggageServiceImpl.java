package io.github.arasdenizhan.bts.management.service.baggage.impl;

import io.github.arasdenizhan.bts.management.dto.BaggageInfoDto;
import io.github.arasdenizhan.bts.management.exception.BaggageServiceException;
import io.github.arasdenizhan.bts.management.mapper.BaggageMapper;
import io.github.arasdenizhan.bts.management.model.entity.Baggage;
import io.github.arasdenizhan.bts.management.model.enums.EventType;
import io.github.arasdenizhan.bts.management.model.websocket.BaggageUpdate;
import io.github.arasdenizhan.bts.management.repository.BaggageRepository;
import io.github.arasdenizhan.bts.management.rest.request.CheckInRequest;
import io.github.arasdenizhan.bts.management.service.baggage.BaggageService;
import io.github.arasdenizhan.bts.management.service.outbox.OutboxEventService;
import io.github.arasdenizhan.bts.management.service.websocket.BaggageWebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaggageServiceImpl implements BaggageService {
    private final BaggageMapper mapper;
    private final BaggageRepository repository;
    private final OutboxEventService outboxEventService;
    private final BaggageWebSocketService baggageWebSocketService;
    private final Clock clock;

    @Override
    @Transactional
    public BaggageInfoDto checkIn(CheckInRequest request) {
        Baggage existingBaggage = repository.findByTag(request.getTag()).orElse(null);
        if(existingBaggage != null){
            throw new BaggageServiceException("Baggage with tag=" + request.getTag() + " already checked-in!");
        }
        LocalDateTime now = LocalDateTime.now(clock);
        log.info("Check-in request, baggageTag={}, flightNumber={}, time={}", request.getTag(), request.getFlightNumber(), now);
        Baggage baggage = mapper.fromRequest(request);
        baggage.setCheckInTime(now);
        baggage.setCurrentStatus(EventType.CHECKED_IN);
        baggage.setCurrentLocation(baggage.getOrigin());
        Baggage savedBaggage = repository.save(baggage);
        log.info("Baggage with baggageTag={} successfully checked in.", request.getTag());
        outboxEventService.saveOutboxEvent(baggage);
        baggageWebSocketService.updateBaggageInfo(baggage);
        return mapper.toDto(savedBaggage);
    }

    @Override
    public List<BaggageUpdate> getAll() {
        return mapper.toUpdate(repository.findAll());
    }

    @Override
    @Transactional
    public void update(String baggageTag) {
        Baggage baggage = repository.findByTag(baggageTag)
                .orElseThrow(() -> new BaggageServiceException("Baggage with tag=" + baggageTag + "not found!"));
        baggage.setLastEventTime(LocalDateTime.now());
        EventType nextStatus = EventType.getNextStatus(baggage.getCurrentStatus());
        if(nextStatus == null){
            log.info("Baggage is on the final status, skipping update.");
            return;
        }
        if(EventType.UNLOADED == nextStatus){
            baggage.setCurrentLocation(baggage.getDestination());
        }
        baggage.setCurrentStatus(nextStatus);
        repository.save(baggage);
        outboxEventService.saveOutboxEvent(baggage);
        baggageWebSocketService.updateBaggageInfo(baggage);
    }
}
