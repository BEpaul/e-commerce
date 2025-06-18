package kr.hhplus.be.server.interfaces.web.coupon.dto.response;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.DiscountType;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CouponResponse {
    private final Long id;
    private final String title;
    private final DiscountType discountType;
    private final Long discountValue;
    private final LocalDateTime expiredAt;

    public CouponResponse(Long id, String title, DiscountType discountType, Long discountValue, LocalDateTime expiredAt) {
        this.id = id;
        this.title = title;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.expiredAt = expiredAt;
    }

    public static CouponResponse of(UserCoupon userCoupon, Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getTitle(),
                coupon.getDiscountType(),
                coupon.getDiscountValue(),
                userCoupon.getExpiredAt()
        );
    }
} 