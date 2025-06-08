package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.AlreadyUsedCouponException;
import kr.hhplus.be.server.common.exception.ExpiredCouponException;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserCoupon {

    private Long id;
    private Long userId;
    private Long couponId;
    private boolean isUsed;
    private LocalDateTime expiredAt;

    @Builder
    public UserCoupon(Long id, Long userId, Long couponId, boolean isUsed, LocalDateTime expiredAt) {
        this.id = id;
        this.userId = userId;
        this.couponId = couponId;
        this.isUsed = isUsed;
        this.expiredAt = expiredAt;
    }

    public void use() {
        if (this.isUsed) {
            throw new AlreadyUsedCouponException("이미 사용된 쿠폰입니다.");
        }

        this.isUsed = true;
    }

    public void isExpired() {
        if (LocalDateTime.now().isAfter(this.expiredAt)) {
            throw new ExpiredCouponException("쿠폰이 만료되었습니다.");
        }
    }
}
