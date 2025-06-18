package kr.hhplus.be.server.interfaces.web.coupon.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class CouponListResponse {
    private final List<CouponResponse> coupons;

    private CouponListResponse(List<CouponResponse> coupons) {
        this.coupons = coupons;
    }

    public static CouponListResponse of(List<CouponResponse> coupons) {
        return new CouponListResponse(coupons);
    }
} 