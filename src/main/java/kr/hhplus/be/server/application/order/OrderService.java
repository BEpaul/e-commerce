package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.point.PointService;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.common.exception.OrderProductEmptyException;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderProduct;
import kr.hhplus.be.server.domain.order.OrderProductRepository;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.infrastructure.external.DataPlatform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final CouponService couponService;
    private final ProductService productService;
    private final PointService pointService;
    private final DataPlatform dataPlatform;

    /**
     * 주문 생성
     */
    @Transactional
    public Order createOrder(Order order, List<OrderProduct> orderProducts) {
        if (orderProducts == null || orderProducts.isEmpty()) {
            throw new OrderProductEmptyException("주문 상품이 존재하지 않습니다.");
        }

        // 주문 상품 재고 감소 및 총 가격 계산
        long totalPrice = 0L;
        for (OrderProduct orderProduct : orderProducts) {
            Product product = productService.getProduct(orderProduct.getProductId());
            product.decreaseStock(orderProduct.getQuantity());
            totalPrice += product.getPrice() * orderProduct.getQuantity();
        }

        // 쿠폰 사용
        if (order.getUserCouponId() != null) {
            totalPrice = couponService.calculateDiscountPrice(order.getUserCouponId(), totalPrice);
            couponService.useCoupon(order.getUserCouponId());
            order.applyCoupon();
        }

        // 주문 총액 설정
        order.calculateTotalAmount(totalPrice);

        // 잔액 차감
        pointService.usePoint(order.getUserId(), totalPrice);

        // 데이터 플랫폼 (외부 시스템) 주문 정보 전송
        dataPlatform.sendData(Payment.from(order.getUserId(), PaymentMethod.POINT, totalPrice));

        // 주문 저장
        Order savedOrder = orderRepository.save(order);

        // 주문 상품 저장
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.assignOrderId(savedOrder.getId());
            orderProductRepository.save(orderProduct);
        }

        return savedOrder;
    }
}
