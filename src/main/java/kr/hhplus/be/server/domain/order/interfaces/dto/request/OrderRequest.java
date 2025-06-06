package kr.hhplus.be.server.domain.order.interfaces.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {
    @NotNull(message = "사용자 ID는 필수입니다.")
    @Min(value = 1, message = "사용자 ID는 1 이상이어야 합니다.")
    private Long userId;

    @Min(value = 1, message = "쿠폰 ID는 1 이상이어야 합니다.")
    private Long userCouponId;

    @NotNull(message = "주문 상품 목록은 필수입니다.")
    private List<OrderProductRequest> orderProducts;
} 