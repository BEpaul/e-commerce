package kr.hhplus.be.server.common.exception;

public class NegativeChargePointException extends RuntimeException {
    public NegativeChargePointException(String message) {
        super(message);
    }
}
