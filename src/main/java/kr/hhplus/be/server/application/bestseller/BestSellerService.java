package kr.hhplus.be.server.application.bestseller;

import kr.hhplus.be.server.common.exception.BestSellerNotFoundException;
import kr.hhplus.be.server.domain.bestseller.BestSeller;
import kr.hhplus.be.server.infrastructure.persistence.bestseller.BestSellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BestSellerService {

    private final BestSellerRepository bestSellerRepository;

    @Transactional(readOnly = true)
    public List<BestSeller> getTopProducts(LocalDateTime date) {
        List<BestSeller> bestSellers = bestSellerRepository.findByTopDateOrderByRankingAsc(date);
        
        if (bestSellers.isEmpty()) {
            throw new BestSellerNotFoundException("해당 날짜의 베스트셀러 데이터가 존재하지 않습니다.");
        }
        
        return bestSellers;
    }
}
