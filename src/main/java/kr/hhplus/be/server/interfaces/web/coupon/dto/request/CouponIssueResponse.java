package kr.hhplus.be.server.interfaces.web.coupon.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CouponIssueResponse {

    private Long couponeId;

    public static CouponIssueResponse from(Long couponeId) {
        return CouponIssueResponse.builder()
                .couponeId(couponeId)
                .build();
    }
}
