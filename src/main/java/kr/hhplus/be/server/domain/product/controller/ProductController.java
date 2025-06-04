package kr.hhplus.be.server.domain.product.controller;

import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.domain.product.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    /**
     * 상품 단건 조회
     */
    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProduct(Long productId) {
        ProductResponse productResponse = ProductResponse.builder()
                .id(productId)
                .name("상품 A")
                .price(10000L)
                .stock(50L)
                .build();

        return ApiResponse.success(productResponse, "상품 조회 성공");
    }

    /**
     * 상품 목록 조회
     */
    @GetMapping()
    public ApiResponse<List<ProductResponse>> getProducts() {
        List<ProductResponse> productResponses = List.of(
                ProductResponse.builder()
                        .id(1L)
                        .name("상품 A")
                        .price(10000L)
                        .stock(50L)
                        .build(),
                ProductResponse.builder()
                        .id(2L)
                        .name("상품 B")
                        .price(20000L)
                        .stock(30L)
                        .build()
        );

        return ApiResponse.success(productResponses, "상품 목록 조회 성공");
    }
}
