package kr.hhplus.be.server.interfaces.web.order.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderResponse {
    private Long orderId;

    public static OrderResponse from(Long orderId) {
        return OrderResponse.builder()
                .orderId(orderId)
                .build();
    }
} 