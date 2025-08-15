package io.github.arasdenizhan.bts.management.model.websocket;

import io.github.arasdenizhan.bts.management.model.enums.EventType;
import io.github.arasdenizhan.bts.management.model.enums.Location;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaggageUpdate {
    private String tag;
    private String flightNumber;
    private EventType status;
    private LocalDateTime lastUpdate;
    private Location location;
}
