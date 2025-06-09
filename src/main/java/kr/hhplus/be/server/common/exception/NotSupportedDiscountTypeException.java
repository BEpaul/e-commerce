package kr.hhplus.be.server.common.exception;

public class NotSupportedDiscountTypeException extends RuntimeException {
    public NotSupportedDiscountTypeException(String message) {
        super(message);
    }
}
