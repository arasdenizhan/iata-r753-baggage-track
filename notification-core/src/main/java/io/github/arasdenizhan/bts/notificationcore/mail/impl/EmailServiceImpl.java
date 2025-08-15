package io.github.arasdenizhan.bts.notificationcore.mail.impl;

import io.github.arasdenizhan.bts.notificationcore.exception.MailServiceException;
import io.github.arasdenizhan.bts.notificationcore.model.kafka.BaggageEvent;
import io.github.arasdenizhan.bts.notificationcore.model.mail.MailMessage;
import io.github.arasdenizhan.bts.notificationcore.model.mail.enums.MailType;
import io.github.arasdenizhan.bts.notificationcore.mail.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void send(MailMessage message) {
        if(message.getMailType() == null){
            log.info("Not required to send mail. Skipping mail send operation.");
            return;
        }
        BaggageEvent baggageEvent = message.getBaggageEvent();
        try {
            log.info("Mail send operation started for baggageTag={}, mailType={}", baggageEvent.getTag(), message.getMailType());
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            getMailContent(message.getMailType(), baggageEvent, helper);
            log.info("Sending mail for baggageTag={}", baggageEvent.getTag());
            mailSender.send(mimeMessage);
            log.info("Mail Sending successfully completed for baggageTag={}", baggageEvent.getTag());
        } catch (Exception e) {
            log.error("Mail send operation failed for baggageTag={}!", baggageEvent.getTag(), e);
            throw new MailServiceException("Mail send operation failed!", e);
        }
    }

    private void getMailContent(MailType mailType, BaggageEvent baggageEvent, MimeMessageHelper helper) throws MessagingException {
        log.info("Mail template generation started for baggageTag={}", baggageEvent.getTag());

        Context context = new Context();
        context.setVariable("passengerName", baggageEvent.getPassengerName());
        context.setVariable("flightNumber", baggageEvent.getFlightNumber());
        context.setVariable("bagTag", baggageEvent.getTag());
        context.setVariable("checkedInAt", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                .format(baggageEvent.getTimestamp()));
        context.setVariable("helpUrl", "denizhanairlines.com");
        context.setVariable("departureAirport", baggageEvent.getOrigin());
        context.setVariable("arrivalAirport", baggageEvent.getDestination());

        String htmlContent = templateEngine.process(mailType.getTemplate(), context);

        helper.setFrom("info@denizhanairlines.com");
        helper.setTo("test@mail.com");
        helper.setSubject(mailType.getTitle());
        helper.setText(htmlContent, true);
        log.info("Mail template generation finished successfully for baggageTag={}", baggageEvent.getTag());
    }
}
