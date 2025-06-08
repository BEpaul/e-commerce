package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.common.exception.OrderProductEmptyException;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderProduct;
import kr.hhplus.be.server.domain.order.OrderProductRepository;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderProductRepository orderProductRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private List<OrderProduct> orderProducts;

    @BeforeEach
    void setUp() {
        order = Order.builder()
            .id(1L)
            .userId(100L)
            .userCouponId(200L)
            .totalAmount(30000L)
            .status(OrderStatus.WAITING)
            .isCouponApplied(true)
            .build();

        orderProducts = List.of(
            OrderProduct.builder()
                .id(1L)
                .productId(100L)
                .orderId(1L)
                .unitPrice(15000L)
                .quantity(2L)
                .build(),
            OrderProduct.builder()
                .id(2L)
                .productId(200L)
                .orderId(1L)
                .unitPrice(15000L)
                .quantity(1L)
                .build()
        );
    }

    @Test
    void 주문_생성에_성공한다() {
        // given
        given(orderRepository.save(any(Order.class))).willReturn(order);

        // when
        Order result = orderService.createOrder(order, orderProducts);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo(100L);
        assertThat(result.getTotalAmount()).isEqualTo(30000L);
        
        then(orderRepository).should(times(1)).save(any(Order.class));
        then(orderProductRepository).should(times(orderProducts.size())).save(any(OrderProduct.class));
    }

    @Test
    void 주문_생성_시_주문_상품이_없는_경우() {
        // given
        List<OrderProduct> emptyOrderProducts = List.of();

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(order, emptyOrderProducts))
                .isInstanceOf(OrderProductEmptyException.class);

        then(orderRepository).should(never()).save(any(Order.class));
        then(orderProductRepository).should(never()).save(any(OrderProduct.class));
    }
} 