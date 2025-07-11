package kr.hhplus.be.server.domain.order.event;

import kr.hhplus.be.server.infrastructure.external.orderinfo.DataPlatform;
import kr.hhplus.be.server.interfaces.web.order.dto.event.OrderCompletedEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCompletedEventListener {

    private final DataPlatform dataPlatform;

    /**
     * 주문 완료 이벤트 처리
     * AFTER_COMMIT: 트랜잭션이 성공적으로 커밋된 후에만 실행
     * @Async: 비동기 처리로 응답 시간 단축
     */
    @Async("eventTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCompletedEvent(OrderCompletedEvent event) {
        try {
            log.info("주문 완료 이벤트 처리 시작 - 주문 ID: {}, 사용자 ID: {}", 
                event.getOrder().getId(), event.getOrder().getUserId());
            
            // 이벤트를 DTO로 변환
            OrderCompletedEventDto eventDto = OrderCompletedEventDto.from(event);
            
            // 데이터 플랫폼으로 전송
            boolean isSuccess = dataPlatform.sendOrderData(eventDto);
            
            if (isSuccess) {
                log.info("데이터 플랫폼 전송 완료 - 주문 ID: {}, 사용자 ID: {}", 
                    event.getOrder().getId(), event.getOrder().getUserId());
            } else {
                log.error("데이터 플랫폼 전송 실패 - 주문 ID: {}, 사용자 ID: {}", 
                    event.getOrder().getId(), event.getOrder().getUserId());
            }
            
        } catch (Exception e) {
            log.error("주문 완료 이벤트 처리 중 오류 발생 - 주문 ID: {}", 
                event.getOrder().getId(), e);
        }
    }
} 