package kr.hhplus.be.server.application.bestseller;

import kr.hhplus.be.server.domain.bestseller.BestSeller;
import kr.hhplus.be.server.infrastructure.persistence.bestseller.BestSellerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BestSellerServiceTest {
    @InjectMocks
    private BestSellerService bestSellerService;

    @Mock
    private BestSellerRepository bestSellerRepository;

    @Test
    void 오늘의_상위상품_5개를_조회한다() {
        // given
        LocalDateTime today = LocalDateTime.now();
        List<BestSeller> expectedBestSellers = Arrays.asList(
            BestSeller.builder().id(1L).productId(101L).rank(1L).topDate(today).build(),
            BestSeller.builder().id(2L).productId(102L).rank(2L).topDate(today).build(),
            BestSeller.builder().id(3L).productId(103L).rank(3L).topDate(today).build(),
            BestSeller.builder().id(4L).productId(104L).rank(4L).topDate(today).build(),
            BestSeller.builder().id(5L).productId(105L).rank(5L).topDate(today).build()
        );

        given(bestSellerRepository.findByTopDateOrderByRankAsc(today))
                .willReturn(expectedBestSellers);

        // when
        List<BestSeller> actualBestSellers = bestSellerService.getTopProducts(today);

        // then
        assertNotNull(actualBestSellers);
        assertEquals(5, actualBestSellers.size());
        assertEquals(1L, actualBestSellers.get(0).getRank());
        assertEquals(5L, actualBestSellers.get(4).getRank());
    }

    @Test
    void 존재하지_않는_날짜의_상품을_조회하면_예외가_발생한다() {
        // given
        LocalDateTime nonExistentDate = LocalDateTime.now().plusDays(1);
        given(bestSellerRepository.findByTopDateOrderByRankAsc(nonExistentDate))
                .willReturn(Collections.emptyList());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            bestSellerService.getTopProducts(nonExistentDate);
        });
    }
}