package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.point.PointService;
import kr.hhplus.be.server.common.exception.ApiException;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.infrastructure.external.DataPlatform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static kr.hhplus.be.server.common.exception.ErrorCode.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private DataPlatform dataPlatform;

    @Mock
    private PointService pointService;

    @Nested
    class Describe_processPayment {

        private Payment payment;
        private Long userId;
        private Point point;

        @BeforeEach
        void setUp() {
            payment = Payment.create(1L, PaymentMethod.POINT, 10000L);
            userId = 1L;
            point = Point.builder()
                .userId(userId)
                .volume(50000L)
                .build();
            lenient().when(pointService.usePoint(any(), any())).thenReturn(point);
        }

        @Test
        void 결제_정보가_없으면_예외가_발생한다() {
            assertThatThrownBy(() -> paymentService.processPayment(null, userId))
                .isInstanceOf(ApiException.class)
                .hasMessage(PAYMENT_INFO_NOT_EXIST.getMessage());
        }

        @Test
        void 결제가_성공하면_결제_상태를_APPROVED로_변경한다() {
            // given
            given(dataPlatform.sendData(payment)).willReturn(true);

            // when
            paymentService.processPayment(payment, userId);

            // then
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.APPROVED);
            then(paymentRepository).should().save(payment);
            then(pointService).should().usePoint(eq(userId), eq(10000L));
        }

        @Test
        void 결제가_실패하면_결제_상태를_CANCELED로_변경하고_예외가_발생한다() {
            // given
            given(dataPlatform.sendData(payment)).willReturn(false);

            // when & then
            assertThatThrownBy(() -> paymentService.processPayment(payment, userId))
                .isInstanceOf(ApiException.class)
                .hasMessage(PAYMENT_PROCESS_ERROR.getMessage());
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELED);
            then(paymentRepository).should().save(payment);
            then(pointService).should().usePoint(eq(userId), eq(10000L));
        }
    }
}
