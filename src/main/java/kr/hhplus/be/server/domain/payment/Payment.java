package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class Payment {
    private Long id;
    private Long orderId;
    private PaymentMethod paymentMethod;
    private Long amount;
    private PaymentStatus status;
    private LocalDateTime approvedAt;
    private LocalDateTime canceledAt;

    public Payment(Long orderId, PaymentMethod paymentMethod, Long amount) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
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