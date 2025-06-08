package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.common.exception.NotFoundCouponException;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final UserCouponRepository userCouponRepository;

    public void useCoupon(Long couponId) {
        UserCoupon userCoupon = userCouponRepository.findByCouponId(couponId)
                .orElseThrow(() -> new NotFoundCouponException("존재하지 않는 쿠폰입니다."));

        userCoupon.use();
        userCouponRepository.save(userCoupon);
    }
}
