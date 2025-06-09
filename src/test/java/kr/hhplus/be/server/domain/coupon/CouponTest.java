package kr.hhplus.be.server.domain.coupon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CouponTest {

    private Coupon amountCoupon;

    @BeforeEach
    void setUp() {
        amountCoupon = Coupon.builder()
                .id(1L)
                .discountValue(3000L)
                .discountType(DiscountType.AMOUNT)
                .title("3000원 할인 쿠폰")
                .stock(10L)
                .build();
    }

    @Nested
    @DisplayName("정액 쿠폰 테스트")
    class AmountCouponTest {

        @Test
        void 정액_쿠폰이_정상적으로_적용된다() {
            // given
            Long productPrice = 10000L;
            Long expectedPrice = 7000L;

            // when
            Long discountedPrice = amountCoupon.apply(productPrice);

            // then
            assertThat(discountedPrice).isEqualTo(expectedPrice);
        }

        @Test
        void 정액_쿠폰의_할인금액이_상품_가격보다_크면_0원을_반환한다() {
            // given
            Long productPrice = 2000L;
            Long expectedPrice = 0L;

            // when
            Long discountedPrice = amountCoupon.apply(productPrice);

            // then
            assertThat(discountedPrice).isEqualTo(expectedPrice);
        }

        @ParameterizedTest
        @CsvSource({
                "10000, 20, 8000",
                "10000, 0, 10000",
                "10000, 100, 0"
        })
        void 정률_쿠폰이_정상적으로_적용된다(Long productPrice, Long discountRate, Long expectedPrice) {
            // given
            Coupon coupon = Coupon.builder()
                    .id(3L)
                    .discountValue(discountRate)
                    .discountType(DiscountType.PERCENT)
                    .title(discountRate + "% 할인 쿠폰")
                    .stock(10L)
                    .build();

            // when
            Long discountedPrice = coupon.apply(productPrice);

            // then
            assertThat(discountedPrice).isEqualTo(expectedPrice);
        }

        @Test
        void 쿠폰_재고가_0이면_예외가_발생한다() {
            // given
            Coupon coupon = Coupon.builder()
                    .id(4L)
                    .discountValue(3000L)
                    .discountType(DiscountType.AMOUNT)
                    .title("3000원 할인 쿠폰")
                    .stock(0L)
                    .build();

            // when & then
            assertThatThrownBy(() -> coupon.apply(10000L))
                    .isInstanceOf(OutOfCouponStockException.class)
                    .hasMessage("쿠폰 재고가 없습니다.");
        }

        @Test
        void 음수_할인값으로_쿠폰을_생성하면_예외가_발생한다() {
            // when & then
            assertThatThrownBy(() -> Coupon.builder()
                    .id(5L)
                    .discountValue(-1000L)
                    .discountType(DiscountType.AMOUNT)
                    .title("잘못된 쿠폰")
                    .stock(10L)
                    .build())
                    .isInstanceOf(InvalidCouponValueException.class)
                    .hasMessage("할인값은 0보다 커야 합니다.");
        }
    }
}
