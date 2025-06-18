package kr.hhplus.be.server.infrastructure.persistence.bestseller;

import kr.hhplus.be.server.domain.bestseller.BestSeller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BestSellerRepository extends JpaRepository<BestSeller, Long> {
    @Query("SELECT b FROM BestSeller b WHERE DATE(b.topDate) = DATE(:date) ORDER BY b.ranking ASC")
    List<BestSeller> findByTopDateOrderByRankingAsc(@Param("date") LocalDateTime date);
}
