package kr.hhplus.be.server.interfaces.web.bestseller.dto.response;

import kr.hhplus.be.server.domain.bestseller.BestSeller;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BestSellerResponse {
    private Long id;
    private String name;
    private Long price;
    private Long stock;
    private Long rank;

    public static BestSellerResponse from(BestSeller bestSeller) {
        return BestSellerResponse.builder()
                .id(bestSeller.getId())
                .name(bestSeller.getName())
                .price(bestSeller.getPrice())
                .stock(bestSeller.getStock())
                .rank(bestSeller.getRank())
                .build();
    }
}
