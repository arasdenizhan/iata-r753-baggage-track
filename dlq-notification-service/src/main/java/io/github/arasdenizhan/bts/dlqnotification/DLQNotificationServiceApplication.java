package io.github.arasdenizhan.bts.dlqnotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "io.github.arasdenizhan.bts")
public class DLQNotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DLQNotificationServiceApplication.class, args);
    }
}
