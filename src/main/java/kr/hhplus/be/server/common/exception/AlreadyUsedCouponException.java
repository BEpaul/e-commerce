package kr.hhplus.be.server.common.exception;

public class AlreadyUsedCouponException extends RuntimeException {
    public AlreadyUsedCouponException(String message) {
        super(message);
    }
}
