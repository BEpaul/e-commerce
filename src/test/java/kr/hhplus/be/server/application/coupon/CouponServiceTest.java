package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.common.exception.AlreadyUsedCouponException;
import kr.hhplus.be.server.common.exception.NotOwnedUserCouponException;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import org.junit.jupiter.api.BeforeEach;
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
        Long userCouponId = 1L;
        given(userCouponRepository.findById(userCouponId))
            .willReturn(Optional.of(userCoupon));

        // when
        couponService.useCoupon(userCouponId);

        // then
        then(userCouponRepository).should(times(1)).findById(userCouponId);
        then(userCouponRepository).should(times(1)).save(any(UserCoupon.class));
    }

    @Test
    void 사용자가_가지고_있지_않은_쿠폰_사용_시_예외가_발생한다() {
        // given
        Long userCouponId = 999L;
        given(userCouponRepository.findById(userCouponId))
            .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> couponService.useCoupon(userCouponId))
                .isInstanceOf(NotOwnedUserCouponException.class);

        then(userCouponRepository).should(times(1)).findById(userCouponId);
        then(userCouponRepository).should(never()).save(any(UserCoupon.class));
    }

    @Test
    void 이미_사용된_쿠폰_사용_시_예외가_발생한다() {
        // given
        Long userCouponId = 1L;
        UserCoupon usedCoupon = UserCoupon.builder()
            .id(userCouponId)
            .userId(100L)
            .couponId(200L)
            .isUsed(true)
            .expiredAt(LocalDateTime.now().plusDays(7))
            .build();

        given(userCouponRepository.findById(userCouponId))
            .willReturn(Optional.of(usedCoupon));

        // when & then
        assertThatThrownBy(() -> couponService.useCoupon(userCouponId))
            .isInstanceOf(AlreadyUsedCouponException.class)
            .hasMessage("이미 사용된 쿠폰입니다.");

        then(userCouponRepository).should(times(1)).findById(userCouponId);
        then(userCouponRepository).should(never()).save(any(UserCoupon.class));
    }
} 