package kr.hhplus.be.server.domain.coupon;

import java.util.Optional;

public interface UserCouponRepository {
    void save(UserCoupon userCoupon);
    Optional<UserCoupon> findById(Long userCouponId);
}
