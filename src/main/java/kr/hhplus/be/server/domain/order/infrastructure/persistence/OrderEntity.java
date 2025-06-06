package kr.hhplus.be.server.domain.order.infrastructure.persistence;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.domain.model.Order;
import kr.hhplus.be.server.domain.order.domain.model.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class OrderEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long userId;
    private Long totalAmount;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    private boolean isCouponApplied;
    private Long userCouponId;

    public static OrderEntity from(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.userId = order.getUserId();
        entity.totalAmount = order.getTotalAmount();
        entity.status = order.getStatus();
        entity.isCouponApplied = order.isCouponApplied();
        entity.userCouponId = order.getUserCouponId();
        return entity;
    }
    
    public Order toDomain() {
        return Order.create(
            this.userId,
            this.totalAmount,
            this.isCouponApplied,
            this.userCouponId
        );
    }
}
