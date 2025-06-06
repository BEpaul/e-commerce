package kr.hhplus.be.server.domain.order.domain.model;

import lombok.Getter;

@Getter
public class Order {
    private Long id;
    private Long userId;
    private Long totalAmount;
    private OrderStatus status;
    private boolean isCouponApplied;
    private Long userCouponId;


    public static Order create(Long userId, Long totalAmount, boolean isCouponApplied, Long userCouponId) {
        Order order = new Order();
        order.userId = userId;
        order.totalAmount = totalAmount;
        order.status = OrderStatus.WAITING;
        order.isCouponApplied = isCouponApplied;
        order.userCouponId = userCouponId;

        return order;
    }
}
