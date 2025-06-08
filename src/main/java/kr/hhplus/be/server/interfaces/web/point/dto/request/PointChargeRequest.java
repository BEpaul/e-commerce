package kr.hhplus.be.server.interfaces.web.point.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "포인트 충전 요청 DTO")
@Getter
public class PointChargeRequest {

    @Schema(description = "사용자 ID", example = "1")
    @Positive
    private Long userId;

    @Schema(description = "충전할 포인트 금액", example = "50000")
    @Positive
    private Long amount;

    @Builder
    private PointChargeRequest(Long userId, Long amount) {
        this.userId = userId;
        this.amount = amount;
    }
}
