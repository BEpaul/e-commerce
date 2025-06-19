package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.common.exception.NotFoundCouponException;
import kr.hhplus.be.server.common.exception.NotOwnedUserCouponException;
import kr.hhplus.be.server.common.exception.OutOfStockCouponException;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import kr.hhplus.be.server.interfaces.web.coupon.dto.response.CouponListResponse;
import kr.hhplus.be.server.interfaces.web.coupon.dto.response.CouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    @Transactional
    public void useCoupon(Long userCouponId) {
        UserCoupon userCoupon = findUserCouponById(userCouponId);
        userCoupon.use();
        userCouponRepository.save(userCoupon);
    }

    @Transactional
    public Long calculateDiscountPrice(Long userCouponId, Long totalPrice) {
        UserCoupon userCoupon = findUserCouponById(userCouponId);
        userCoupon.isExpired();

        Coupon coupon = findCouponById(userCoupon.getCouponId());
        return coupon.apply(totalPrice);
    }

    /**
     * 쿠폰 발급
     */
    @Transactional
    @Retryable(
            value = OptimisticLockingFailureException.class,
            maxAttempts = 5, // 최대 5번 재시도
            backoff = @Backoff(delay = 10) // 재시도 간 50ms 대기
    )
    public UserCoupon issueCoupon(Long userId, Long couponId) {
        Coupon coupon = findCouponById(couponId);

        if (coupon.getStock() <= 0) {
            throw new OutOfStockCouponException("쿠폰 재고가 부족합니다.");
        }

        coupon.decreaseStock();
        couponRepository.save(coupon);
        
        UserCoupon userCoupon = UserCoupon.of(userId, couponId);
        return userCouponRepository.save(userCoupon);
    }

    /**
     * 사용자가 보유한 쿠폰 목록을 조회
     */
    @Transactional(readOnly = true)
    public CouponListResponse getUserCoupons(Long userId) {
        List<UserCoupon> userCoupons = userCouponRepository.findUnusedByUserId(userId);

        if (userCoupons.isEmpty()) {
            return CouponListResponse.of(List.of());
        }

        List<CouponResponse> couponResponses = userCoupons.stream()
                .map(userCoupon -> {
                    Coupon coupon = findCouponById(userCoupon.getCouponId());
                    return CouponResponse.of(userCoupon, coupon);
                })
                .collect(Collectors.toList());

        return CouponListResponse.of(couponResponses);
    }

    private UserCoupon findUserCouponById(Long userCouponId) {
        return userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new NotOwnedUserCouponException("사용자가 갖고 있지 않은 쿠폰입니다."));
    }

    private Coupon findCouponById(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new NotFoundCouponException("쿠폰이 존재하지 않습니다."));
    }
}
