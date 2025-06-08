package kr.hhplus.be.server.common.exception;

public class ExpiredCouponException extends RuntimeException {
    public ExpiredCouponException(String message) {
        super(message);
    }
}
