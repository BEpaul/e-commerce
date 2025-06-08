package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.common.exception.NotOwnedUserCouponException;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final UserCouponRepository userCouponRepository;

    public void useCoupon(Long userCouponId) {
        UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new NotOwnedUserCouponException("사용자가 갖고 있지 않은 쿠폰입니다."));

        userCoupon.use();
        userCouponRepository.save(userCoupon);
    }
}
