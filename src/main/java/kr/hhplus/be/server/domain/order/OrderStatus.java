package kr.hhplus.be.server.domain.order;

public enum OrderStatus {
    COMPLETED, // 주문 완료
    CANCELED, // 주문 취소
    WAITING, // 주문 대기 중
    PENDING, // 주문 보류
    FAILED // 주문 실패
}
