package kr.hhplus.be.server.domain.coupon;

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
}
