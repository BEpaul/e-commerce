package kr.hhplus.be.server.interfaces.web.coupon.dto.response;

import kr.hhplus.be.server.domain.coupon.DiscountType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserCouponResponse {
    private Long id;
    private String title;
    private DiscountType discountType;
    private Long discountValue;
    private LocalDateTime expiredAt;
}