package kr.hhplus.be.server.domain.point.repository;

import kr.hhplus.be.server.domain.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {
    Optional<Point> findByUserId(Long userId);
}
