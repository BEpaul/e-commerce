package kr.hhplus.be.server.domain.point.exception;

public class ExceedsMaximumPointException extends RuntimeException {
    public ExceedsMaximumPointException(String message) {
        super(message);
    }
}
