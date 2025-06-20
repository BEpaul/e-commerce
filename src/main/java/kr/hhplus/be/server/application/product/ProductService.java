package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.common.exception.NotFoundProductException;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.infrastructure.persistence.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 상품 단건 조회
     */
    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundProductException("상품이 존재하지 않습니다."));

    }

    /**
     * 상품 목록 조회
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 비관적 락을 사용한 상품 조회 (재고 차감용)
     */
    @Transactional
    public Product getProductWithPessimisticLock(Long productId) {
        return productRepository.findByIdWithPessimisticLock(productId)
                .orElseThrow(() -> new NotFoundProductException("상품이 존재하지 않습니다."));
    }
}
