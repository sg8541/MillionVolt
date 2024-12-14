package kr.co.milionvolt.ifive.service.reservation;

import kr.co.milionvolt.ifive.entity.ReservationRedis;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReservationRedisService {
    public List<ReservationRedis> findReservationInfoByUserId(int userId);


}
