package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
public class Payment {
    private Long id;
    private Long orderId;
    private PaymentMethod paymentMethod;
    private Long amount;
    private PaymentStatus status;
    private LocalDateTime approvedAt;
    private LocalDateTime canceledAt;

    public static Payment from(Long orderId, PaymentMethod paymentMethod, Long amount) {
        return Payment.builder()
                .orderId(orderId)
                .paymentMethod(paymentMethod)
                .amount(amount)
                .status(PaymentStatus.PENDING)
                .build();
    }

    public void approve() {
        this.status = PaymentStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = PaymentStatus.CANCELED;
        this.canceledAt = LocalDateTime.now();
    }
} 