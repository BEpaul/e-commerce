package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.common.exception.NotFoundCouponException;
import kr.hhplus.be.server.common.exception.NotOwnedUserCouponException;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public void useCoupon(Long userCouponId) {
        UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new NotOwnedUserCouponException("사용자가 갖고 있지 않은 쿠폰입니다."));

        userCoupon.use();
        userCouponRepository.save(userCoupon);
    }

    public Long calculateDiscountPrice(Long userCouponId, Long totalPrice) {
        UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new NotOwnedUserCouponException("사용자가 갖고 있지 않은 쿠폰입니다."));

        userCoupon.isExpired();

        Coupon coupon = couponRepository.findById(userCoupon.getCouponId())
                .orElseThrow(() -> new NotFoundCouponException("쿠폰이 존재하지 않습니다."));

        return coupon.apply(totalPrice);
    }
}
