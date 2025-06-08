package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.common.exception.NotFoundUserException;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.infrastructure.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

    private static final Long MAXIMUM_POINT = 3_000_000L;
    private final PointRepository pointRepository;

    @Transactional
    public Point chargePoint(Long userId, Long chargeAmount) {
        Point point = pointRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));

        point.charge(chargeAmount);
        point.addChargePointHistory(chargeAmount);
        
        return pointRepository.save(point);
    }

    @Transactional(readOnly = true)
    public Point getPoint(Long userId) {
        return pointRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));
    }
}
