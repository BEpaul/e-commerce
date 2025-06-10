package kr.hhplus.be.server.domain.order;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Order {
   private Long id;
   private Long userId;
   private Long userCouponId;
   private Long totalAmount;
   private OrderStatus status;
   private boolean isCouponApplied;

   @Builder
   private Order(Long id, Long userId, Long userCouponId, Long totalAmount, OrderStatus status, boolean isCouponApplied) {
       this.id = id;
       this.userId = userId;
       this.userCouponId = userCouponId;
       this.totalAmount = totalAmount;
       this.status = status;
       this.isCouponApplied = isCouponApplied;
   }

    public void calculateTotalAmount(Long totalAmount) {
       this.totalAmount = totalAmount;
    }
}