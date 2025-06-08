package kr.hhplus.be.server.common.exception;

public class NotFoundCouponException extends RuntimeException {
    public NotFoundCouponException(String message) {
        super(message);
    }
}
