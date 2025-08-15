package io.github.arasdenizhan.bts.notification.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.arasdenizhan.bts.notificationcore.exception.KafkaServiceException;
import io.github.arasdenizhan.bts.notificationcore.exception.MailServiceException;
import io.github.arasdenizhan.bts.notificationcore.model.kafka.BaggageEvent;
import io.github.arasdenizhan.bts.notificationcore.model.mail.MailMessage;
import io.github.arasdenizhan.bts.notificationcore.model.mail.enums.MailType;
import io.github.arasdenizhan.bts.notificationcore.mail.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {
    private static final String BAGGAGE_TOPIC = "baggage.tracking";
    private static final String MAIL_DLQ_TOPIC = "mail.send.failures.dlq";

    private final KafkaTemplate<String, MailMessage> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    @KafkaListener(topics = BAGGAGE_TOPIC, groupId = "notification")
    public void consume(String message){
        try {
            log.info("Incoming kafka message, trying to parse message.");
            BaggageEvent baggageEvent = objectMapper.reader().readValue(message, BaggageEvent.class);
            log.info("BaggageEvent with baggageTag={} successfully parsed.", baggageEvent.getTag());
            tryToSendMail(baggageEvent);
        } catch (IOException e) {
            log.error("Error occurred while reading incoming kafka message!", e);
            throw new KafkaServiceException("Incoming kafka message not parsable!", e);
        }
    }

    @Retryable(
            retryFor = {MailServiceException.class},
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void tryToSendMail(BaggageEvent baggageEvent) {
        MailMessage mailMessage = new MailMessage(MailType.getType(baggageEvent.getEventType()), baggageEvent);
        emailService.send(mailMessage);
    }

    @Recover
    public void recover(MailServiceException e, MailMessage mail) {
        kafkaTemplate.send(MAIL_DLQ_TOPIC, mail);
        log.error("Mail send is failed after retries, mail message sent to the DLQ: {}", mail, e);
    }
}
