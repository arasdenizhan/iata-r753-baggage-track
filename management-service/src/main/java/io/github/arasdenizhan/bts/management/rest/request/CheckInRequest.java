package io.github.arasdenizhan.bts.management.rest.request;

import io.github.arasdenizhan.bts.management.model.enums.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckInRequest {
    private String tag;
    private String passengerName;
    private String flightNumber;
    private Location origin;
    private Location destination;
}
