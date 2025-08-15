package io.github.arasdenizhan.bts.management.service.baggage;

import io.github.arasdenizhan.bts.management.dto.BaggageInfoDto;
import io.github.arasdenizhan.bts.management.model.websocket.BaggageUpdate;
import io.github.arasdenizhan.bts.management.rest.request.CheckInRequest;

import java.util.List;

public interface BaggageService {
    BaggageInfoDto checkIn(CheckInRequest request);
    List<BaggageUpdate> getAll();
    void update(String baggageTag);
}
