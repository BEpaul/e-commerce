package kr.hhplus.be.server.domain.point.exception;

public class NegativeChargePointException extends RuntimeException {
    public NegativeChargePointException(String message) {
        super(message);
    }
}
