package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.common.exception.ExceedsMaximumPointException;
import kr.hhplus.be.server.common.exception.NegativeChargePointException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PointTest {

    @Test
    void 포인트_충전에_성공한다() {
        // given
        Long userId = 1L;
        Long initialPoint = 10000L;
        Long chargeAmount = 50000L;
        Point point = Point.create(userId, initialPoint);

        // when
        point.charge(chargeAmount);

        // then
        assertThat(point.getVolume()).isEqualTo(initialPoint + chargeAmount);
    }

    @ParameterizedTest
    @ValueSource(longs = {-1000, -1, 0})
    void 음수_또는_0포인트를_충전하면_예외가_발생한다(Long chargeAmount) {
        // given
        Long userId = 1L;
        Long initialPoint = 10000L;
        Point point = Point.create(userId, initialPoint);

        // when & then
        assertThatThrownBy(() -> point.charge(chargeAmount))
                .isInstanceOf(NegativeChargePointException.class);
    }

    @Test
    void 포인트_충전_후_300만_포인트가_넘으면_예외가_발생한다() {
         // given
        Long userId = 1L;
        Long initialPoint = 2500000L;
        Long chargeAmount = 600000L; // 충전 후 총액이 3100000이 되어 예외 발생

        Point point = Point.create(userId, initialPoint);

        // when & then
        assertThatThrownBy(() -> point.charge(chargeAmount))
                .isInstanceOf(ExceedsMaximumPointException.class);
    }
}
