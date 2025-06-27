package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.payment.PaymentService;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.common.exception.ApiException;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderProduct;
import kr.hhplus.be.server.domain.order.OrderProductRepository;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.hhplus.be.server.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final CouponService couponService;
    private final ProductService productService;
    private final PaymentService paymentService;

    /**
     * 주문 생성
     * 1. 주문 상품 유효성 검사
     * 2. 상품 재고 감소
     * 3. 총 가격 계산
     * 4. 쿠폰 할인 적용
     * 5. 쿠폰 사용 처리
     * 6. 주문 정보 저장
     * 7. 결제 처리
     * 8. 주문 상품 정보 저장
     * 9. 주문 상태 업데이트
     */
    @Transactional
    public Order createOrder(Order order, List<OrderProduct> orderProducts) {
        validateOrderProducts(orderProducts);

        decreaseProductStocks(orderProducts);
        long totalPrice = calculateTotalPrice(orderProducts);
        totalPrice = calculateDiscountedPrice(order, totalPrice);
        processCouponUsage(order);

        Order savedOrder = saveOrder(order, totalPrice);
        processPayment(savedOrder, totalPrice);
        saveOrderProducts(savedOrder, orderProducts);

        return savedOrder;
    }

    private void validateOrderProducts(List<OrderProduct> orderProducts) {
        if (orderProducts == null || orderProducts.isEmpty()) {
            throw new ApiException(ORDER_PRODUCT_EMPTY);
        }
    }

    private void decreaseProductStocks(List<OrderProduct> orderProducts) {
        for (OrderProduct orderProduct : orderProducts) {
            Product product = productService.getProductWithPessimisticLock(orderProduct.getProductId());
            product.decreaseStock(orderProduct.getQuantity());
        }
    }

    private long calculateTotalPrice(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .mapToLong(it -> {
                    Product product = productService.getProduct(it.getProductId());
                    return product.getPrice() * it.getQuantity();
                })
                .sum();
    }

    private long calculateDiscountedPrice(Order order, long totalPrice) {
        if (order.getUserCouponId() == null) {
            return totalPrice;
        }
        return couponService.calculateDiscountPrice(order.getUserCouponId(), totalPrice);
    }

    private void processCouponUsage(Order order) {
        if (order.getUserCouponId() == null) {
            return;
        }
        couponService.useCoupon(order.getUserCouponId());
        order.applyCoupon();
    }

    private Order saveOrder(Order order, long totalPrice) {
        order.calculateTotalAmount(totalPrice);
        return orderRepository.save(order);
    }

    private void processPayment(Order order, long totalPrice) {
        try {
            paymentService.processPayment(
                Payment.create(order.getId(), PaymentMethod.POINT, totalPrice),
                order.getUserId()
            );
            order.success();
        } catch (Exception e) {
            order.fail();
            throw new ApiException(PAYMENT_FAILED);
        }
    }

    private void saveOrderProducts(Order order, List<OrderProduct> orderProducts) {
        for (OrderProduct orderProduct : orderProducts) {
            Product product = productService.getProduct(orderProduct.getProductId());
            orderProduct.assignOrderInfo(order.getId(), product.getPrice());
            orderProductRepository.save(orderProduct);
        }
    }
}
