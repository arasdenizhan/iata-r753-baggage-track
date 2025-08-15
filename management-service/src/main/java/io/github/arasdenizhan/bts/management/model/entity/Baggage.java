package io.github.arasdenizhan.bts.management.model.entity;

import io.github.arasdenizhan.bts.management.model.enums.EventType;
import io.github.arasdenizhan.bts.management.model.enums.Location;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
public class Baggage {
    @Id
    @Setter(value = AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tag;

    private String passengerName;

    private String flightNumber;

    @Enumerated(value = EnumType.STRING)
    private Location origin;

    @Enumerated(value = EnumType.STRING)
    private Location destination;

    private LocalDateTime checkInTime;

    private LocalDateTime lastEventTime;

    @Enumerated(value = EnumType.STRING)
    private EventType currentStatus;

    @Enumerated(value = EnumType.STRING)
    private Location currentLocation;

    private boolean isDelayed;
}
