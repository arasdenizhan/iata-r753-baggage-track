package io.github.arasdenizhan.bts.notificationcore.mail;

import io.github.arasdenizhan.bts.notificationcore.model.mail.MailMessage;

public interface EmailService {
    void send(MailMessage message);
}
