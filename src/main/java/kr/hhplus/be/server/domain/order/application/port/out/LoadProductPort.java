package kr.hhplus.be.server.domain.order.application.port.out;

import java.util.List;

public interface LoadProductPort {
    ProductInfo loadProduct(Long productId);
    
    record ProductInfo(
        Long productId,
        String name,
        Long price,
        Long stock
    ) {}
} 