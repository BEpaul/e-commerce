package kr.hhplus.be.server.interfaces.web.bestseller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.bestseller.BestSellerService;
import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.domain.bestseller.BestSeller;
import kr.hhplus.be.server.interfaces.web.bestseller.dto.response.BestSellerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "베스트셀러", description = "베스트셀러 관련 API")
@RestController
@RequestMapping("/api/v1/bestsellers")
@RequiredArgsConstructor
public class BestSellerController {

    private final BestSellerService bestSellerService;

    /**
     * 상위 상품 목록 조회
     */
    @Operation(summary = "상위 상품 목록 조회", description = "현재 날짜 기준으로 상위 상품 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<List<BestSellerResponse>> getTopProducts() {
        List<BestSeller> bestSellers = bestSellerService.getTopProducts(LocalDateTime.now());

        List<BestSellerResponse> responses = bestSellers.stream()
                .map(BestSellerResponse::from)
                .collect(Collectors.toList());

        return ApiResponse.success(responses, "상위 상품 목록 조회 성공");
    }
} 