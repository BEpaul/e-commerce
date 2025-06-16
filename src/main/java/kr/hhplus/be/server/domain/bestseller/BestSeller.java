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
    private String name;
    private Long price;
    private Integer stock;
    private Long rank;
    private LocalDateTime topDate;

    @Builder
    public BestSeller(Long id, String name, Long price, Integer stock, Long rank, LocalDateTime topDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.rank = rank;
        this.topDate = topDate;
    }
}
