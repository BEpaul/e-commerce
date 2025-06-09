package kr.hhplus.be.server.common.exception;

public class InvalidCouponValueException extends RuntimeException {
    public InvalidCouponValueException(String message) {
        super(message);
    }
}
