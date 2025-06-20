package kr.hhplus.be.server.common.exception;

public class PaymentProcessException extends RuntimeException {
    public PaymentProcessException(String message) {
        super(message);
    }

    public PaymentProcessException(String message, Throwable cause) {
        super(message, cause);
    }
} 