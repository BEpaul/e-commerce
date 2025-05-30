package kr.hhplus.be.server.domain.point.controller;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.domain.point.dto.request.PointChargeRequest;
import kr.hhplus.be.server.domain.point.dto.response.PointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/points")
public class PointController {

    @Operation(summary = "포인트 조회", description = "사용자의 포인트를 조회합니다.")
    @GetMapping("/{userId}")
    public ApiResponse<PointResponse> getPoints(@PathVariable Long userId) {
        PointResponse pointResponse = PointResponse.builder()
                .point(10000L)
                .build();

        return ApiResponse.success(pointResponse, "포인트 조회 성공");
    }

    @Operation(summary = "포인트 충전", description = "사용자의 포인트를 충전합니다.")
    @PostMapping("/charge")
    public ApiResponse<PointResponse> chargePoints(@RequestBody PointChargeRequest pointChargeRequest) {
        long chargedPoints = 5000 + pointChargeRequest.getAmount();

        PointResponse response = PointResponse.builder()
                .point(chargedPoints)
                .build();

        return ApiResponse.success(response, "포인트 충전 성공");
    }
}
