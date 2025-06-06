package kr.hhplus.be.server.domain.order.interfaces.web;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.domain.order.application.OrderService;
import kr.hhplus.be.server.domain.order.interfaces.dto.request.OrderProductRequest;
import kr.hhplus.be.server.domain.order.interfaces.dto.request.OrderRequest;
import kr.hhplus.be.server.domain.order.interfaces.dto.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    // UI or API 레이어

    private final OrderService orderService;

    /**
     * 주문 생성
     */
    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    @PostMapping
    public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        // 현재는 첫 번째 상품만 주문하도록 구현
        // TODO: 여러 상품 주문 기능 구현
        OrderProductRequest firstProduct = orderRequest.getOrderProducts().get(0);

        OrderResponse response = orderService.createOrder(
            orderRequest.getUserId(),
            firstProduct.getProductId(),
            firstProduct.getQuantity(),
            orderRequest.getUserCouponId()
        );
        
        return ApiResponse.success(response, "주문 생성 성공");
    }
}
