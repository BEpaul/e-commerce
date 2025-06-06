package kr.hhplus.be.server.domain.order.application;

import kr.hhplus.be.server.domain.order.application.port.out.LoadProductPort;
import kr.hhplus.be.server.domain.order.domain.exception.OutOfStockError;
import kr.hhplus.be.server.domain.order.domain.model.Order;
import kr.hhplus.be.server.domain.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.domain.order.interfaces.dto.response.OrderResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final LoadProductPort loadProductPort;

    // 주문 생성
    public OrderResponse createOrder(Long userId, Long productId, int quantity, Long userCouponId) {
        // 1. 재고 검사 및 상품 재고 차감
        // TODO: 여러 상품 주문 로직으로 변경
        LoadProductPort.ProductInfo productInfo = loadProductPort.loadProduct(productId);

        if (productInfo.stock() < quantity) {
            throw new OutOfStockError("재고가 부족합니다.");
        }

        // 2. 가격 계산
        Long totalPrice = productInfo.price() * quantity;

        // 3. 쿠폰 적용
        // TODO: 쿠폰 적용 로직 추가

        // 4. 주문 생성
        Order order = Order.create(userId, totalPrice, false, userCouponId);
        
        // 5. 주문 저장
        Order savedOrder = orderRepository.save(order);

        // 6. 응답 생성
        return new OrderResponse(savedOrder.getId());
    }
}
