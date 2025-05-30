package kr.hhplus.be.server.domain.point.controller;

import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.domain.point.dto.request.PointChargeRequest;
import kr.hhplus.be.server.domain.point.dto.response.PointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/points")
public class PointController {

    @GetMapping("/{userId}")
    public ApiResponse<PointResponse> getPoints(@PathVariable Long userId) {
        PointResponse pointResponse = PointResponse.builder()
                .point(10000L)
                .build();

        return ApiResponse.success(pointResponse, "포인트 조회 성공");
    }

    @PostMapping("/charge")
    public ApiResponse<PointResponse> chargePoints(@RequestBody PointChargeRequest pointChargeRequest) {
        long chargedPoints = 5000 + pointChargeRequest.getAmount();

        PointResponse response = PointResponse.builder()
                .point(chargedPoints)
                .build();

        return ApiResponse.success(response, "포인트 충전 성공");
    }
}
