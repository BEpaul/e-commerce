package kr.hhplus.be.server.interfaces.web.bestseller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.bestseller.BestSellerService;
import kr.hhplus.be.server.domain.bestseller.BestSeller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BestSellerController.class)
public class BestSellerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BestSellerService bestSellerService;

    private List<BestSeller> mockBestSellers;

    @BeforeEach
    void setUp() {
        mockBestSellers = List.of(
            BestSeller.builder()
                .id(1L)
                .name("메론킥")
                .price(3000L)
                .stock(100L)
                .rank(1L)
                .topDate(LocalDateTime.now())
                .build(),
            BestSeller.builder()
                .id(2L)
                .name("엄마손")
                .price(2000L)
                .stock(50L)
                .rank(2L)
                .topDate(LocalDateTime.now())
                .build(),
            BestSeller.builder()
                .id(3L)
                .name("깡깡깡")
                .price(500L)
                .stock(1000L)
                .rank(3L)
                .topDate(LocalDateTime.now())
                .build(),
            BestSeller.builder()
                .id(4L)
                .name("스파게티")
                .price(10000L)
                .stock(30L)
                .rank(4L)
                .topDate(LocalDateTime.now())
                .build(),
            BestSeller.builder()
                .id(5L)
                .name("한우")
                .price(300000L)
                .stock(5L)
                .rank(5L)
                .topDate(LocalDateTime.now())
                .build()
        );
    }

    @Test
    void 상위_상품_목록_조회에_성공한다() throws Exception {
        // given
        given(bestSellerService.getTopProducts(any())).willReturn(mockBestSellers);

        // when & then
        mockMvc.perform(get("/api/v1/bestsellers"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.message").value("상위 상품 목록 조회 성공"))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(5))
            .andExpect(jsonPath("$.data[0].id").value(1))
            .andExpect(jsonPath("$.data[0].name").value("메론킥"))
            .andExpect(jsonPath("$.data[0].price").value(3000))
            .andExpect(jsonPath("$.data[0].stock").value(100))
            .andExpect(jsonPath("$.data[0].rank").value(1))
            .andExpect(jsonPath("$.data[1].id").value(2))
            .andExpect(jsonPath("$.data[1].name").value("엄마손"))
            .andExpect(jsonPath("$.data[1].price").value(2000))
            .andExpect(jsonPath("$.data[1].stock").value(50))
            .andExpect(jsonPath("$.data[1].rank").value(2))
            .andExpect(jsonPath("$.data[2].id").value(3))
            .andExpect(jsonPath("$.data[2].name").value("깡깡깡"))
            .andExpect(jsonPath("$.data[2].price").value(500))
            .andExpect(jsonPath("$.data[2].stock").value(1000))
            .andExpect(jsonPath("$.data[2].rank").value(3))
            .andExpect(jsonPath("$.data[3].id").value(4))
            .andExpect(jsonPath("$.data[3].name").value("스파게티"))
            .andExpect(jsonPath("$.data[3].price").value(10000))
            .andExpect(jsonPath("$.data[3].stock").value(30))
            .andExpect(jsonPath("$.data[3].rank").value(4))
            .andExpect(jsonPath("$.data[4].id").value(5))
            .andExpect(jsonPath("$.data[4].name").value("한우"))
            .andExpect(jsonPath("$.data[4].price").value(300000))
            .andExpect(jsonPath("$.data[4].stock").value(5))
            .andExpect(jsonPath("$.data[4].rank").value(5));
    }
}
