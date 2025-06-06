package kr.hhplus.be.server.domain.order.infrastructure.persistence;

import kr.hhplus.be.server.domain.order.domain.model.Order;
import kr.hhplus.be.server.domain.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderJpaRepository implements OrderRepository {
    
    private final OrderEntityRepository orderEntityRepository;
    
    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = OrderEntity.from(order);
        OrderEntity savedOrderEntity = orderEntityRepository.save(orderEntity);
        return savedOrderEntity.toDomain();
    }
}
