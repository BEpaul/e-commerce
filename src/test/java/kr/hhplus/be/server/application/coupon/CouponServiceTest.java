package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.common.exception.AlreadyUsedCouponException;
import kr.hhplus.be.server.common.exception.NotFoundCouponException;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private UserCouponRepository userCouponRepository;

    @InjectMocks
    private CouponService couponService;

    private UserCoupon userCoupon;

    @BeforeEach
    void setUp() {
        userCoupon = UserCoupon.builder()
            .id(1L)
            .userId(100L)
            .couponId(200L)
            .isUsed(false)
            .expiredAt(LocalDateTime.now().plusDays(7))
            .build();
    }

    @Test
    void 쿠폰_사용에_성공한다() {
        // given
        Long couponId = 200L;
        given(userCouponRepository.findByCouponId(couponId))
            .willReturn(Optional.of(userCoupon));

        // when
        couponService.useCoupon(couponId);

        // then
        then(userCouponRepository).should(times(1)).findByCouponId(couponId);
        then(userCouponRepository).should(times(1)).save(any(UserCoupon.class));
    }

    @Test
    void 존재하지_않는_쿠폰_사용_시_예외가_발생한다() {
        // given
        Long couponId = 999L;
        given(userCouponRepository.findByCouponId(couponId))
            .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> couponService.useCoupon(couponId))
            .isInstanceOf(NotFoundCouponException.class)
            .hasMessage("존재하지 않는 쿠폰입니다.");

        then(userCouponRepository).should(times(1)).findByCouponId(couponId);
        then(userCouponRepository).should(never()).save(any(UserCoupon.class));
    }

    @Test
    void 이미_사용된_쿠폰_사용_시_예외가_발생한다() {
        // given
        Long couponId = 200L;
        UserCoupon usedCoupon = UserCoupon.builder()
            .id(1L)
            .userId(100L)
            .couponId(200L)
            .isUsed(true)
            .expiredAt(LocalDateTime.now().plusDays(7))
            .build();

        given(userCouponRepository.findByCouponId(couponId))
            .willReturn(Optional.of(usedCoupon));

        // when & then
        assertThatThrownBy(() -> couponService.useCoupon(couponId))
            .isInstanceOf(AlreadyUsedCouponException.class)
            .hasMessage("이미 사용된 쿠폰입니다.");

        then(userCouponRepository).should(times(1)).findByCouponId(couponId);
        then(userCouponRepository).should(never()).save(any(UserCoupon.class));
    }
} 