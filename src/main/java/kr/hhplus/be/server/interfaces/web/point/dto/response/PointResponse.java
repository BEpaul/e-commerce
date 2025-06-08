package kr.hhplus.be.server.interfaces.web.point.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PointResponse {
    private Long point;

    public static PointResponse from(Long point) {
        return PointResponse.builder()
                .point(point)
                .build();
    }
}
