package kr.hhplus.be.server.application.bestseller;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.infrastructure.persistence.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.ZSetOperations;

@Slf4j
@Service
@RequiredArgsConstructor
public class BestSellerRankingService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductRepository productRepository;
    
    private static final String RANKING_KEY_PREFIX = "ranking:daily:";
    private static final String PRODUCT_PREFIX = "product:";
    private static final int TTL_DAYS = 31;
    
    /**
     * 새로운 날짜의 랭킹 키를 생성하고 모든 상품을 0점으로 초기화
     */
    public void initializeDailyRanking(LocalDate date) {
        String rankingKey = getRankingKey(date);
        
        log.info("새로운 날짜 랭킹 초기화 시작 - 키: {}", rankingKey);
        
        // 모든 상품 ID 조회
        List<Long> productIds = productRepository.findAll().stream()
                .map(Product::getId)
                .toList();
        
        if (productIds.isEmpty()) {
            log.warn("초기화할 상품이 없습니다.");
            return;
        }
        
        // Redis Sorted Set에 모든 상품을 0점으로 추가
        for (Long productId : productIds) {
            String member = PRODUCT_PREFIX + productId;
            redisTemplate.opsForZSet().add(rankingKey, member, 0.0);
        }
        
        // TTL 설정 (31일)
        redisTemplate.expire(rankingKey, TTL_DAYS, TimeUnit.DAYS);
        
        log.info("새로운 날짜 랭킹 초기화 완료 - 키: {}, 상품 수: {}", rankingKey, productIds.size());
    }
    
    /**
     * 상품 판매량 증가
     */
    public void incrementSalesCount(LocalDate date, Long productId, Long quantity) {
        String rankingKey = getRankingKey(date);
        String member = PRODUCT_PREFIX + productId;
        
        Double newScore = redisTemplate.opsForZSet().incrementScore(rankingKey, member, quantity);
        log.debug("판매량 증가 - 키: {}, 상품: {}, 수량: {}, 새로운 점수: {}", 
                rankingKey, member, quantity, newScore);
    }
    
    /**
     * 베스트셀러 TOP5 조회
     */
    public Set<ZSetOperations.TypedTuple<Object>> getTop5Products(LocalDate date) {
        String rankingKey = getRankingKey(date);
        
        Set<ZSetOperations.TypedTuple<Object>> topProducts = redisTemplate.opsForZSet()
                .reverseRangeWithScores(rankingKey, 0, 4);
        
        log.debug("베스트셀러 TOP5 조회 - 키: {}, 결과: {}", rankingKey, topProducts);
        return topProducts;
    }
    
    /**
     * 특정 날짜의 모든 상품 판매량 조회
     */
    public Set<ZSetOperations.TypedTuple<Object>> getAllProductSales(LocalDate date) {
        String rankingKey = getRankingKey(date);
        
        Set<ZSetOperations.TypedTuple<Object>> allProducts = redisTemplate.opsForZSet()
                .rangeWithScores(rankingKey, 0, -1);
        
        log.debug("전체 상품 판매량 조회 - 키: {}, 결과 수: {}", rankingKey, allProducts.size());
        return allProducts;
    }
    
    /**
     * 랭킹 키 생성
     */
    private String getRankingKey(LocalDate date) {
        return RANKING_KEY_PREFIX + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
} 