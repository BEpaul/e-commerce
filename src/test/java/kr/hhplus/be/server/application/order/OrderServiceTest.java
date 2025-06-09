package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.common.exception.OrderProductEmptyException;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderProduct;
import kr.hhplus.be.server.domain.order.OrderProductRepository;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.product.Product;
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

    @Mock
    private ProductService productService;

    @Mock
    private CouponService couponService;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private List<OrderProduct> orderProducts;
    private Product product;

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .id(1L)
                .userId(100L)
                .totalAmount(30000L)
                .build();

        product = Product.builder()
                .name("테스트 상품")
                .price(10000L)
                .stock(10L)
                .description("테스트 상품 설명")
                .build();

        OrderProduct orderProduct = OrderProduct.builder()
                .id(1L)
                .orderId(1L)
                .productId(1L)
                .quantity(3L)
                .build();

        orderProducts = List.of(orderProduct);
    }

    @Test
    void 주문_생성에_성공한다() {
        // given
        given(orderRepository.save(any(Order.class))).willReturn(order);
        given(productService.getProduct(any(Long.class))).willReturn(product);

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