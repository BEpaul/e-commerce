package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.AlreadyUsedCouponException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserCouponTest {

    @Test
    void 쿠폰_사용_성공() {
        // given
        UserCoupon userCoupon = UserCoupon.builder()
            .id(1L)
            .userId(100L)
            .couponId(200L)
            .isUsed(false)
            .expiredAt(LocalDateTime.now().plusDays(7))
            .build();

        // when
        userCoupon.use();

        // then
        assertThat(userCoupon.isUsed()).isTrue();
    }

    @Test
    void 이미_사용된_쿠폰_사용_시_예외_발생() {
        // given
        UserCoupon userCoupon = UserCoupon.builder()
            .id(1L)
            .userId(100L)
            .couponId(200L)
            .isUsed(true)
            .expiredAt(LocalDateTime.now().plusDays(7))
            .build();

        // when & then
        assertThatThrownBy(() -> userCoupon.use())
            .isInstanceOf(AlreadyUsedCouponException.class)
            .hasMessage("이미 사용된 쿠폰입니다.");
    }
} 