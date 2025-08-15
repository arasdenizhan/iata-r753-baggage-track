package io.github.arasdenizhan.bts.management.service.websocket;

import io.github.arasdenizhan.bts.management.model.entity.Baggage;

public interface BaggageWebSocketService {
    void updateBaggageInfo(Baggage baggage);
}
