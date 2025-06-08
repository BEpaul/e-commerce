package kr.hhplus.be.server.common.exception;

public class NotOwnedUserCouponException extends RuntimeException {
    public NotOwnedUserCouponException(String message) {
        super(message);
    }
}
