package kr.hhplus.be.server.domain.order.domain.exception;

public class OutOfStockError extends RuntimeException{
    public OutOfStockError(String message) {
        super(message);
    }
}
