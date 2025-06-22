package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.payment.PaymentService;
import kr.hhplus.be.server.application.point.PointService;
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
    private final PointService pointService;
    private final PaymentService paymentService;

    /**
     * 주문 생성
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

    private long calculateTotalPrice(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .mapToLong(it -> {
                    Product product = productService.getProduct(it.getProductId());
                    return product.getPrice() * it.getQuantity();
                })
                .sum();
    }

    private void decreaseProductStocks(List<OrderProduct> orderProducts) {
        for (OrderProduct orderProduct : orderProducts) {
            Product product = productService.getProductWithPessimisticLock(orderProduct.getProductId());
            product.decreaseStock(orderProduct.getQuantity());
        }
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
        pointService.usePoint(order.getUserId(), totalPrice);
        return orderRepository.save(order);
    }

    private void processPayment(Order order, long totalPrice) {
        try {
            paymentService.processPayment(
                Payment.create(order.getId(), PaymentMethod.POINT, totalPrice)
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
