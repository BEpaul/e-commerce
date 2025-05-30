package kr.hhplus.be.server.domain.point.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "포인트 충전 요청 DTO")
@Getter
public class PointChargeRequest {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "충전할 포인트 금액", example = "5000")
    private Long amount;

    @Builder
    private PointChargeRequest(Long userId, Long amount) {
        this.userId = userId;
        this.amount = amount;
    }
}
