package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.common.exception.OrderProductEmptyException;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderProduct;
import kr.hhplus.be.server.domain.order.OrderProductRepository;
import kr.hhplus.be.server.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    /**
     * 주문 생성
     */
    public Order createOrder(Order order, List<OrderProduct> orderProducts) {
        if (orderProducts == null || orderProducts.isEmpty()) {
            throw new OrderProductEmptyException("주문 상품이 존재하지 않습니다.");
        }

        Order savedOrder = orderRepository.save(order);

        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.assignOrderId(savedOrder.getId());
            orderProductRepository.save(orderProduct);
        }

        return savedOrder;
    }
}
