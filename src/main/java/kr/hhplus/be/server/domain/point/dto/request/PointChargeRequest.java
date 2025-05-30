package kr.hhplus.be.server.domain.point.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PointChargeRequest {

    private Long userId;
    private Long amount;

    @Builder
    private PointChargeRequest(Long userId, Long amount) {
        this.userId = userId;
        this.amount = amount;
    }
}
