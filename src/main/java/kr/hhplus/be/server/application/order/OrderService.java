package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.payment.PaymentService;
import kr.hhplus.be.server.application.point.PointService;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.common.exception.FailedPaymentException;
import kr.hhplus.be.server.common.exception.OrderProductEmptyException;
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

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final CouponService couponService;
    private final ProductService productService;
    private final PointService pointService;
    private final PaymentService paymentService;

    @Transactional
    public Order createOrder(Order order, List<OrderProduct> orderProducts) {
        if (orderProducts == null || orderProducts.isEmpty()) {
            throw new OrderProductEmptyException("주문 상품이 존재하지 않습니다.");
        }

        long totalPrice = 0L;
        for (OrderProduct orderProduct : orderProducts) {
            Product product = productService.getProduct(orderProduct.getProductId());
            product.decreaseStock(orderProduct.getQuantity());
            totalPrice += product.getPrice() * orderProduct.getQuantity();
        }

        if (order.getUserCouponId() != null) {
            totalPrice = couponService.calculateDiscountPrice(order.getUserCouponId(), totalPrice);
            couponService.useCoupon(order.getUserCouponId());
            order.applyCoupon();
        }

        order.calculateTotalAmount(totalPrice);

        pointService.usePoint(order.getUserId(), totalPrice);

        Order savedOrder = orderRepository.save(order);

        boolean result = paymentService.processPayment(Payment.create(order.getId(), PaymentMethod.POINT, totalPrice));

        if (!result) {
            throw new FailedPaymentException("결제에 실패했습니다.");
        }

        order.success();

        for (OrderProduct orderProduct : orderProducts) {
            Product product = productService.getProduct(orderProduct.getProductId());
            orderProduct.assignOrderInfo(savedOrder.getId(), product.getPrice());
            orderProductRepository.save(orderProduct);
        }

        return savedOrder;
    }
}
