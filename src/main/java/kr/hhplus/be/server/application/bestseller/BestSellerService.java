package kr.hhplus.be.server.application.bestseller;

import kr.hhplus.be.server.common.exception.ApiException;
import kr.hhplus.be.server.domain.bestseller.BestSeller;
import kr.hhplus.be.server.infrastructure.persistence.bestseller.BestSellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static kr.hhplus.be.server.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class BestSellerService {

    private final BestSellerRepository bestSellerRepository;

    @Transactional(readOnly = true)
    public List<BestSeller> getTopProducts(LocalDateTime date) {
        List<BestSeller> bestSellers = bestSellerRepository.findByTopDateOrderByRankingAsc(date);
        
        if (bestSellers.isEmpty()) {
            throw new ApiException(BESTSELLER_NOT_FOUND);
        }
        
        return bestSellers;
    }
}
