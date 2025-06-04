package kr.hhplus.be.server.domain.product.dto.response;

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
}
