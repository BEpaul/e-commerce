package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;
    
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    private Long amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    
    private LocalDateTime approvedAt;
    private LocalDateTime canceledAt;

    public static Payment create(Long orderId, PaymentMethod paymentMethod, Long amount) {
        Payment payment = new Payment();
        payment.orderId = orderId;
        payment.paymentMethod = paymentMethod;
        payment.amount = amount;
        payment.status = PaymentStatus.PENDING;
        
        return payment;
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