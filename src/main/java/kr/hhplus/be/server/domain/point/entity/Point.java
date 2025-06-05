package kr.hhplus.be.server.domain.point.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.BaseTimeEntity;
import kr.hhplus.be.server.common.exception.ExceedsMaximumPointException;
import kr.hhplus.be.server.common.exception.NegativeChargePointException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long volume;

    @Builder
    public Point(Long userId, Long volume) {
        this.userId = userId;
        this.volume = volume;
    }

    public void charge(Long amount) {
        if (amount <= 0) {
            throw new NegativeChargePointException("충전 금액은 0보다 커야 합니다.");
        }

        if (this.volume + amount > 3_000_000) {
            throw new ExceedsMaximumPointException("충전 후 포인트가 300만을 초과할 수 없습니다.");
        }

        this.volume += amount;
    }
}
