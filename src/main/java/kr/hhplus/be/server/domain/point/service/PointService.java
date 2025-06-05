package kr.hhplus.be.server.domain.point.service;

import kr.hhplus.be.server.common.exception.ExceedsMaximumPointException;
import kr.hhplus.be.server.common.exception.NegativeChargePointException;
import kr.hhplus.be.server.common.exception.NotFoundUserException;
import kr.hhplus.be.server.domain.point.entity.Point;
import kr.hhplus.be.server.domain.point.repository.PointRepository;
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
    public Long getPoint(Long userId) {
        Point point = pointRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없습니다."));
        return point.getVolume();
    }
}
