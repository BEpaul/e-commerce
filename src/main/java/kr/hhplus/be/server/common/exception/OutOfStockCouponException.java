package kr.hhplus.be.server.common.exception;

public class OutOfStockCouponException extends RuntimeException {
    public OutOfStockCouponException(String message) {
        super(message);
    }
}
