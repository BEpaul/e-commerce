package kr.hhplus.be.server.domain.bestseller;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BestSeller extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bestseller_id")
    private Long id;
    private Long productId;
    private Long rank;
    private LocalDateTime topDate;

    @Builder
    public BestSeller(Long id, Long productId, Long rank, LocalDateTime topDate) {
        this.id = id;
        this.productId = productId;
        this.rank = rank;
        this.topDate = topDate;
    }
}
