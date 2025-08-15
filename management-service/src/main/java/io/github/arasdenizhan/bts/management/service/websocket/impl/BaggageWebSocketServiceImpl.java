package io.github.arasdenizhan.bts.management.service.websocket.impl;

import io.github.arasdenizhan.bts.management.model.entity.Baggage;
import io.github.arasdenizhan.bts.management.model.websocket.BaggageUpdate;
import io.github.arasdenizhan.bts.management.service.websocket.BaggageWebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BaggageWebSocketServiceImpl implements BaggageWebSocketService {
    private static final String TOPIC = "/topic/luggage-updates";
    private final SimpMessagingTemplate template;

    @Override
    public void updateBaggageInfo(Baggage baggage) {
        BaggageUpdate baggageUpdate = BaggageUpdate.builder()
                .tag(baggage.getTag())
                .lastUpdate(LocalDateTime.now())
                .status(baggage.getCurrentStatus())
                .flightNumber(baggage.getFlightNumber())
                .location(baggage.getCurrentLocation()).build();
        template.convertAndSend(TOPIC, baggageUpdate);
    }
}
