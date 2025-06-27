package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.point.PointService;
import kr.hhplus.be.server.common.exception.*;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.infrastructure.external.DataPlatform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.hhplus.be.server.common.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final DataPlatform dataPlatform;
    private final PointService pointService;

    /**
     * 결제 처리
     * 1. 중복 결제 체크
     * 2. 포인트 차감
     * 3. 결제 정보 저장 (PENDING 상태)
     * 4. 외부 플랫폼 결제 처리 시작
     */
    @Transactional
    public void processPayment(Payment payment, Long userId) {
        if (payment == null) {
            throw new ApiException(PAYMENT_INFO_NOT_EXIST);
        }

        checkDuplicatePayment(payment);
        deductPoint(payment, userId);

        // 결제 정보를 저장 (이미 PENDING 상태)
        savePayment(payment);

        // 외부 플랫폼 결제 처리 시작
        handleExternalPayment(payment.getId());
    }

    private void checkDuplicatePayment(Payment payment) {
        if (paymentRepository.existsByIdempotencyKey(payment.getIdempotencyKey())) {
            throw new ApiException(DUPLICATE_PAYMENT);
        }
    }

    private void deductPoint(Payment payment, Long userId) {
        if (payment.getPaymentMethod() == PaymentMethod.POINT) {
            pointService.usePoint(userId, payment.getAmount());
        }
    }

//    @Async("taskExecutor")
    protected void handleExternalPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ApiException(PAYMENT_INFO_NOT_EXIST));

        boolean isPaymentSuccess = sendPaymentToDataPlatform(payment);

        if (!isPaymentSuccess) {
            payment.cancel();
            savePayment(payment);
            log.error("Payment processing failed for paymentId: {}", paymentId);
            throw new ApiException(PAYMENT_PROCESSING_FAILED);
        }

        payment.approve();
        savePayment(payment);

        log.info("Payment processing completed for paymentId: {}, success: {}", paymentId, isPaymentSuccess);
    }

    private boolean sendPaymentToDataPlatform(Payment payment) {
        return dataPlatform.sendData(payment);
    }

    private void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }
}
