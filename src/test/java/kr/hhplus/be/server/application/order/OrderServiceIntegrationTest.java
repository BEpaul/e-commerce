package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.common.exception.FailedPaymentException;
import kr.hhplus.be.server.common.exception.OrderProductEmptyException;
import kr.hhplus.be.server.domain.coupon.*;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderProduct;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.infrastructure.external.DataPlatform;
import kr.hhplus.be.server.infrastructure.persistence.point.PointRepository;
import kr.hhplus.be.server.infrastructure.persistence.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @MockitoBean
    private DataPlatform dataPlatform;

    private Long userId = 1L;
    private Long productId;
    private Long userCouponId;
    private Product product;
    private Point point;
    private Coupon coupon;
    private UserCoupon userCoupon;

    @BeforeEach
    void setUp() {
        // 상품 생성
        product = Product.builder()
                .name("테스트 상품")
                .price(10000L)
                .stock(10L)
                .description("테스트 상품 설명")
                .build();
        productId = productRepository.save(product).getId();

        // 포인트 생성
        point = Point.builder()
                .userId(userId)
                .volume(50000L)
                .build();
        pointRepository.save(point);

        // 쿠폰 생성
        coupon = Coupon.builder()
                .title("테스트 쿠폰")
                .discountType(DiscountType.AMOUNT)
                .discountValue(1000L)
                .stock(10L)
                .build();
        couponRepository.save(coupon);

        // 사용자 쿠폰 생성
        userCoupon = UserCoupon.builder()
                .userId(userId)
                .couponId(coupon.getId())
                .isUsed(false)
                .expiredAt(LocalDateTime.now().plusDays(30))
                .build();
        userCouponId = userCouponRepository.save(userCoupon).getId();

        // 외부 결제 플랫폼 모킹
        given(dataPlatform.sendData(any())).willReturn(true);
    }

    @Nested
    class CreateOrderTest {

        @Test
        void 주문을_생성한다() {
            // given
            Order order = Order.builder()
                    .userId(userId)
                    .userCouponId(userCouponId)
                    .build();

            OrderProduct orderProduct = OrderProduct.builder()
                    .productId(productId)
                    .quantity(2L)
                    .build();

            // when
            Order savedOrder = orderService.createOrder(order, List.of(orderProduct));

            // then
            assertThat(savedOrder.getId()).isNotNull();
            assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.COMPLETED);
            assertThat(savedOrder.getTotalAmount()).isEqualTo(19000L); // 10000 * 2 - 1000(쿠폰)
            assertThat(savedOrder.isCouponApplied()).isTrue();
        }

        @Test
        void 주문_상품이_없으면_예외가_발생한다() {
            // given
            Order order = Order.builder()
                    .userId(userId)
                    .build();

            // when & then
            assertThatThrownBy(() -> orderService.createOrder(order, List.of()))
                    .isInstanceOf(OrderProductEmptyException.class);
        }

        @Test
        void 결제가_실패하면_예외가_발생한다() {
            // given
            Order order = Order.builder()
                    .userId(userId)
                    .build();

            OrderProduct orderProduct = OrderProduct.builder()
                    .productId(productId)
                    .quantity(2L)
                    .build();

            given(dataPlatform.sendData(any())).willReturn(false);

            // when & then
            assertThatThrownBy(() -> orderService.createOrder(order, List.of(orderProduct)))
                    .isInstanceOf(FailedPaymentException.class);
            assertThat(order.getStatus()).isEqualTo(OrderStatus.FAILED);
        }
    }
}
