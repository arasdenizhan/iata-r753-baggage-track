package io.github.arasdenizhan.bts.management.mapper;

import io.github.arasdenizhan.bts.management.dto.BaggageInfoDto;
import io.github.arasdenizhan.bts.management.model.entity.Baggage;
import io.github.arasdenizhan.bts.management.model.websocket.BaggageUpdate;
import io.github.arasdenizhan.bts.management.rest.request.CheckInRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.util.List;

import static io.github.arasdenizhan.bts.management.model.enums.EventType.CHECKED_IN;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface BaggageMapper {
    Baggage fromRequest(CheckInRequest request);
    BaggageInfoDto toDto(Baggage baggage);

    @Mapping(target = "status", source = "currentStatus")
    @Mapping(target = "location", source = "currentLocation")
    @Mapping(target = "lastUpdate", source = ".", qualifiedByName = "getEventTime")
    BaggageUpdate toUpdate(Baggage baggage);

    List<BaggageUpdate> toUpdate(List<Baggage> baggage);

    @Named(value = "getEventTime")
    default LocalDateTime getEventTime(Baggage baggage){
        return CHECKED_IN == baggage.getCurrentStatus() ? baggage.getCheckInTime() : baggage.getLastEventTime();
    }
}
