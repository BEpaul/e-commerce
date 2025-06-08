package kr.hhplus.be.server.domain.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class OrderProduct {

    private Long id;
    private Long productId;
    private Long orderId;
    private Long unitPrice;
    private Long quantity;

    @Builder
    private OrderProduct(Long id, Long productId, Long orderId, Long unitPrice, Long quantity) {
        this.id = id;
        this.productId = productId;
        this.orderId = orderId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public void assignOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
