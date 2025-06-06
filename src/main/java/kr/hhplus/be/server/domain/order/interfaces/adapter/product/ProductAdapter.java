package kr.hhplus.be.server.domain.order.interfaces.adapter.product;

import kr.hhplus.be.server.domain.order.application.port.out.LoadProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductAdapter implements LoadProductPort {
    private final kr.hhplus.be.server.domain.product.service.ProductService productService;

    @Override
    public ProductInfo loadProduct(Long productId) {
        var product = productService.getProduct(productId);
        return new ProductInfo(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getStock()
        );
    }
} 