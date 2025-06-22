package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.common.exception.*;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.infrastructure.external.DataPlatform;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.hhplus.be.server.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final DataPlatform dataPlatform;

    /**
     * 결제 처리
     * 1. 중복 결제 체크
     * 2. 결제 정보 저장
     * 3. 비동기로 결제 처리 시작
     */
    @Transactional
    public void processPayment(Payment payment) {
        if (payment == null) {
            throw new ApiException(PAYMENT_INFO_NOT_EXIST);
        }

        checkDuplicatePayment(payment);
        processPaymentAsync(payment);
    }

    private void checkDuplicatePayment(Payment payment) {
        if (paymentRepository.existsByIdempotencyKey(payment.getIdempotencyKey())) {
            throw new ApiException(DUPLICATE_PAYMENT);
        }
    }

    @Async
    @Transactional
    protected void processPaymentAsync(Payment payment) {
        try {
            boolean isPaymentSuccess = sendPaymentToDataPlatform(payment);
            updatePaymentStatus(payment, isPaymentSuccess);
            savePayment(payment);
        } catch (Exception e) {
            payment.cancel();
            savePayment(payment);
            throw new ApiException(PAYMENT_PROCESS_ERROR);
        }
    }

    private boolean sendPaymentToDataPlatform(Payment payment) {
        return dataPlatform.sendData(payment);
    }

    private void updatePaymentStatus(Payment payment, boolean isSuccess) {
        if (isSuccess) {
            payment.approve();
            return;
        }
        payment.cancel();
    }

    private void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }
}
