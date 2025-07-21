package kr.hhplus.be.server.application.coupon.kafka;

import kr.hhplus.be.server.common.exception.ApiException;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponIssueResult;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import kr.hhplus.be.server.infrastructure.config.kafka.KafkaTopicConstants;
import kr.hhplus.be.server.interfaces.web.coupon.dto.event.CouponIssueRequestEventDto;
import kr.hhplus.be.server.interfaces.web.coupon.dto.event.CouponIssueResultEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static kr.hhplus.be.server.common.exception.ErrorCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueConsumer {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final CouponKafkaEventService couponKafkaEventService;

    /**
     * 쿠폰 발급 요청을 처리하는 Kafka Consumer
     * 같은 쿠폰 ID의 요청은 같은 파티션에서 순차 처리됨
     */
    @KafkaListener(
            topics = KafkaTopicConstants.COUPON_ISSUE_REQUEST,
            groupId = "coupon-issue-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleCouponIssueRequest(
            @Payload CouponIssueRequestEventDto requestEvent,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment ack) {
        
        log.info("쿠폰 발급 요청 처리 시작 - 파티션: {}, 오프셋: {}, 요청 ID: {}, 사용자 ID: {}, 쿠폰 ID: {}",
                partition, offset, requestEvent.getRequestId(), requestEvent.getUserId(), requestEvent.getCouponId());
        
        try {
            CouponIssueResult result = processCouponIssue(requestEvent);
            publishResult(result);

            if (result.isSuccess()) {
                log.info("쿠폰 발급 처리 완료 - 요청 ID: {}, 사용자 ID: {}, 쿠폰 ID: {}, UserCoupon ID: {}, 남은 재고: {}",
                        requestEvent.getRequestId(), requestEvent.getUserId(), requestEvent.getCouponId(),
                        result.getUserCouponId(), result.getRemainingStock());
            } else {
                log.warn("쿠폰 발급 실패 - 요청 ID: {}, 사용자 ID: {}, 쿠폰 ID: {}, 사유: {}",
                        requestEvent.getRequestId(), requestEvent.getUserId(), requestEvent.getCouponId(), result.getErrorMessage());
            }
            
        } catch (Exception e) {
            CouponIssueResult errorResult = createErrorResult(requestEvent, e);
            publishResult(errorResult);

            log.error("쿠폰 발급 처리 중 오류 발생 - 요청 ID: {}, 사용자 ID: {}, 쿠폰 ID: {}",
                    requestEvent.getRequestId(), requestEvent.getUserId(), requestEvent.getCouponId(), e);
        }
        
        ack.acknowledge();
    }

    private CouponIssueResult processCouponIssue(CouponIssueRequestEventDto requestEvent) {
        Coupon coupon = findCouponById(requestEvent.getCouponId());
        
        // 중복 발급 검증
        if (isAlreadyIssued(requestEvent.getUserId(), requestEvent.getCouponId())) {
            return CouponIssueResult.duplicateIssued(requestEvent, "이미 발급받은 쿠폰입니다.");
        }
        
        // 재고 검증
        if (coupon.getStock() <= 0) {
            return CouponIssueResult.outOfStock(requestEvent, "쿠폰 재고가 부족합니다.");
        }
        
        // 쿠폰 발급 처리
        return issueCoupon(requestEvent, coupon);
    }

    private CouponIssueResult issueCoupon(CouponIssueRequestEventDto requestEvent, Coupon coupon) {
        coupon.decreaseStock();
        couponRepository.save(coupon);
        
        UserCoupon userCoupon = UserCoupon.of(requestEvent.getUserId(), requestEvent.getCouponId());
        UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);
        
        return CouponIssueResult.success(requestEvent, savedUserCoupon.getId(), coupon.getStock());
    }

    private void publishResult(CouponIssueResult result) {
        CouponIssueResultEventDto resultEvent = result.toEventDto();
        couponKafkaEventService.publishCouponIssueResult(resultEvent);
    }

    private CouponIssueResult createErrorResult(CouponIssueRequestEventDto requestEvent, Exception e) {
        return CouponIssueResult.error(requestEvent, "쿠폰 발급 처리 중 오류가 발생했습니다: " + e.getMessage());
    }

    private Coupon findCouponById(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new ApiException(COUPON_NOT_FOUND));
    }

    private boolean isAlreadyIssued(Long userId, Long couponId) {
        return userCouponRepository.existsByUserIdAndCouponId(userId, couponId);
    }
}