package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.dto.response.ProductResponse;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 단건 조회")
    void 상품ID로_단일_상품을_조회한다() {
        // given
        Long productId = 1L;
        Product product = Product.builder()
                .name("상품 A")
                .price(10000L)
                .stock(50L)
                .description("상품 A 설명")
                .build();

        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when
        ProductResponse response = productService.getProduct(productId);

        // then
        assertThat(response.getId()).isEqualTo(productId);
        assertThat(response.getName()).isEqualTo("상품 A");
        assertThat(response.getPrice()).isEqualTo(10000L);
        assertThat(response.getStock()).isEqualTo(50L);
        assertThat(response.getDescription()).isEqualTo("상품 A 설명");
    }

    @Test
    @DisplayName("상품 목록 조회")
    void 상품_목록을_조회한다() {
        // given
        List<Product> products = List.of(
                Product.builder()
                        .name("상품 1")
                        .price(10000L)
                        .stock(50L)
                        .description("상품 1 설명")
                        .build(),
                Product.builder()
                        .name("상품 2")
                        .price(20000L)
                        .stock(30L)
                        .description("상품 2 설명")
                        .build()
        );

        given(productRepository.findAll()).willReturn(products);

        // when
        List<ProductResponse> responses = productService.getProducts();

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getName()).isEqualTo("상품 1");
        assertThat(responses.get(1).getName()).isEqualTo("상품 2");
    }
}