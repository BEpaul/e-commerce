package kr.hhplus.be.server.application.bestseller;

import kr.hhplus.be.server.common.exception.ApiException;
import kr.hhplus.be.server.domain.bestseller.BestSeller;
import kr.hhplus.be.server.infrastructure.persistence.bestseller.BestSellerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kr.hhplus.be.server.common.exception.ErrorCode.BESTSELLER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class BestSellerServiceTest {
    private static final Logger log = LoggerFactory.getLogger(BestSellerServiceTest.class);
    @InjectMocks
    private BestSellerService bestSellerService;

    @Mock
    private BestSellerRepository bestSellerRepository;

    private List<BestSeller> createBestSellers(LocalDateTime topDate) {
        return Arrays.asList(
            BestSeller.builder()
                .id(1L)
                .name("상품1")
                .productId(1L)
                .ranking(1L)
                .topDate(topDate)
                .build(),
            BestSeller.builder()
                .id(2L)
                .name("상품2")
                .productId(2L)
                .ranking(2L)
                .topDate(topDate)
                .build(),
            BestSeller.builder()
                .id(3L)
                .name("상품3")
                .productId((3L))
                .ranking(3L)
                .topDate(topDate)
                .build(),
            BestSeller.builder()
                .id(4L)
                .name("상품4")
                .productId(4L)
                .ranking(4L)
                .topDate(topDate)
                .build(),
            BestSeller.builder()
                .id(5L)
                .name("상품5")
                .productId(5L)
                .ranking(5L)
                .topDate(topDate)
                .build()
        );
    }

    private void assertBestSellers(List<BestSeller> bestSellers) {
        assertNotNull(bestSellers);
        assertEquals(5, bestSellers.size());
        assertEquals(1L, bestSellers.get(0).getRanking());
        assertEquals(5L, bestSellers.get(4).getRanking());
    }

    @Test
    void 오늘의_상위상품_5개를_조회한다() {
        // given
        LocalDateTime today = LocalDateTime.now();
        List<BestSeller> expectedBestSellers = createBestSellers(today);

        lenient().when(bestSellerRepository.findByTopDateOrderByRankingAsc(today))
                .thenReturn(expectedBestSellers);

        // when
        List<BestSeller> actualBestSellers = bestSellerService.getTopProducts(today);

        // then
        assertBestSellers(actualBestSellers);
    }

    @Test
    void 시간이_다른_같은_날짜의_상품을_조회한다() {
        // given
        LocalDateTime searchTime = LocalDateTime.of(2024, 3, 18, 15, 30, 0);
        LocalDateTime storedTime = LocalDateTime.of(2024, 3, 18, 0, 0, 0);
        List<BestSeller> expectedBestSellers = createBestSellers(storedTime);

        lenient().when(bestSellerRepository.findByTopDateOrderByRankingAsc(searchTime))
            .thenReturn(expectedBestSellers);

        // when
        List<BestSeller> actualBestSellers = bestSellerService.getTopProducts(searchTime);

        // then
        assertBestSellers(actualBestSellers);
    }

    @Test
    void 존재하지_않는_날짜의_상품을_조회하면_예외가_발생한다() {
        // given
        LocalDateTime nonExistentDate = LocalDateTime.now().plusDays(1);
        lenient().when(bestSellerRepository.findByTopDateOrderByRankingAsc(nonExistentDate))
                .thenReturn(Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> bestSellerService.getTopProducts(nonExistentDate))
                .isInstanceOf(ApiException.class)
                .hasMessage(BESTSELLER_NOT_FOUND.getMessage());
    }
}