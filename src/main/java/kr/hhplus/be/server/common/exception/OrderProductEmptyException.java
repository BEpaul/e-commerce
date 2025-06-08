package kr.hhplus.be.server.common.exception;

public class OrderProductEmptyException extends RuntimeException {
    public OrderProductEmptyException(String message) {
        super(message);
    }
}