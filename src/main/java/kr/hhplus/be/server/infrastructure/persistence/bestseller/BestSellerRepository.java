package kr.hhplus.be.server.infrastructure.persistence.bestseller;

import kr.hhplus.be.server.domain.bestseller.BestSeller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BestSellerRepository extends JpaRepository<BestSeller, Long> {
}
