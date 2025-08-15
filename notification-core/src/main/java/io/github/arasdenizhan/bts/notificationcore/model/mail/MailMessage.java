package io.github.arasdenizhan.bts.notificationcore.model.mail;

import io.github.arasdenizhan.bts.notificationcore.model.kafka.BaggageEvent;
import io.github.arasdenizhan.bts.notificationcore.model.mail.enums.MailType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailMessage {
    private MailType mailType;
    private BaggageEvent baggageEvent;
}
