package io.github.arasdenizhan.bts.notificationcore.model.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaggageEvent {
    private String tag;
    private String flightNumber;
    private LocalDateTime timestamp;
    private String origin;
    private String destination;
    private String passengerName;
    private String eventType;
    private String handlingCarrier;
}
