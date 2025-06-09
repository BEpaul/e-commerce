package kr.hhplus.be.server.interfaces.web.product.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderProductRequest {

    private Long productId;
    private Long quantity;

    @Builder
    public OrderProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
