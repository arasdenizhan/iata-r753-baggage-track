package io.github.arasdenizhan.bts.management.exception;

public class OutboxEventException extends RuntimeException {
    public OutboxEventException(String message, Throwable cause) {
        super(message, cause);
    }
}
