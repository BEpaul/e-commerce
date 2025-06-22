package kr.hhplus.be.server.domain.bestseller;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "bestseller")
public class BestSeller extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bestseller_id")
    private Long id;
    private String name;
    private Long price;
    private Long stock;
    private Long ranking;
    private LocalDateTime topDate;

    @Builder
    public BestSeller(Long id, String name, Long price, Long stock, Long ranking, LocalDateTime topDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.ranking = ranking;
        this.topDate = topDate;
    }
}
