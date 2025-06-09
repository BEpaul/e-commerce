package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.InvalidCouponValueException;
import kr.hhplus.be.server.common.exception.NotSupportedDiscountTypeException;
import kr.hhplus.be.server.common.exception.OutOfStockCouponException;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Coupon {

    private Long id;
    private Long discountValue;
    private DiscountType discountType;
    private String title;
    private Long stock;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder
    public Coupon(Long id, Long discountValue, DiscountType discountType, String title, Long stock, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.discountValue = discountValue;
        this.discountType = discountType;
        this.title = title;
        this.stock = stock;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long apply(Long productPrice) {
        if (this.stock <= 0) {
            throw new OutOfStockCouponException("쿠폰의 재고가 부족합니다.");
        }

        if (discountType == DiscountType.AMOUNT) {
            return Math.max(productPrice - discountValue, 0L);
        } else if (discountType == DiscountType.PERCENT) {
            return productPrice - (productPrice * discountValue / 100);
        }

        throw new NotSupportedDiscountTypeException("지원하지 않는 할인 유형입니다.");
    }
}
