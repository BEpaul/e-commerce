package kr.hhplus.be.server.domain.order.config;

import kr.hhplus.be.server.domain.order.application.OrderService;
import kr.hhplus.be.server.domain.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.domain.order.application.port.out.LoadProductPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {

    @Bean
    public OrderService orderService(
        OrderRepository orderRepository,
        LoadProductPort loadProductPort
    ) {
        return new OrderService(orderRepository, loadProductPort);
    }
} 