package io.github.arasdenizhan.bts.management.dto;

import io.github.arasdenizhan.bts.management.model.enums.EventType;
import io.github.arasdenizhan.bts.management.model.enums.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BaggageInfoDto {
    private String tag;
    private String passengerName;
    private String flightNumber;
    private Location origin;
    private Location destination;
    private LocalDateTime checkInTime;
    private LocalDateTime lastEventTime;
    private EventType currentStatus;
    private Location currentLocation;
    private boolean isDelayed;
}
