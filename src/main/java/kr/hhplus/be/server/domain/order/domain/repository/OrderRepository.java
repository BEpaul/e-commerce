package kr.hhplus.be.server.domain.order.domain.repository;

import kr.hhplus.be.server.domain.order.domain.model.Order;

public interface OrderRepository {
    Order save(Order order);
}
