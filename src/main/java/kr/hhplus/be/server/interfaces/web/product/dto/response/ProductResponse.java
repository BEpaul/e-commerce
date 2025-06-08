package kr.hhplus.be.server.interfaces.web.product.dto.response;

import kr.hhplus.be.server.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private Long price;
    private Long stock;
    private String description;

    public static ProductResponse from(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .description(product.getDescription())
                .build();
    }
}
