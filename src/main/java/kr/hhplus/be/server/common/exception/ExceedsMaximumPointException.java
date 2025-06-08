package kr.hhplus.be.server.common.exception;

public class ExceedsMaximumPointException extends RuntimeException {
    public ExceedsMaximumPointException(String message) {
        super(message);
    }
}
