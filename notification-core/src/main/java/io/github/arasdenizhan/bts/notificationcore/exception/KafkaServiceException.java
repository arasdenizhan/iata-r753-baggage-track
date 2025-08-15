package io.github.arasdenizhan.bts.notificationcore.exception;

public class KafkaServiceException extends RuntimeException {
    public KafkaServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
