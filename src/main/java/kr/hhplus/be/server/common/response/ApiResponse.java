package kr.hhplus.be.server.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private int code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static <T> ApiResponse<T> success(T data, String msg) {
        return ApiResponse.<T>builder()
                .code(200)
                .message(msg)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(int code, String msg) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(msg)
                .build();
    }

    @Builder
    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
