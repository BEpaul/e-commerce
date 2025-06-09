package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.common.exception.OrderProductEmptyException;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderProduct;
import kr.hhplus.be.server.domain.order.OrderProductRepository;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.interfaces.web.product.dto.request.OrderProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final CouponService couponService;
    private final ProductService productService;

    /**
     * 주문 생성
     */
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
            couponService.calculateDiscountPrice(order.getUserCouponId(), totalPrice);
            couponService.useCoupon(order.getUserCouponId());
        }

        return orderRepository.save(order);
    }
}
