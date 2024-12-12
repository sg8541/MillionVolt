package kr.co.milionvolt.ifive.service.reservation;

import kr.co.milionvolt.ifive.entity.ReservationRedis;
import kr.co.milionvolt.ifive.mapper.UserMapper;
import kr.co.milionvolt.ifive.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationRedisServiceImpl implements ReservationRedisService {
    @Autowired
    private ReservationRepository reservationRepository;


    @Override
    public List<ReservationRedis> findReservationInfoByUserId(int userId) {
        List<ReservationRedis> list= (List<ReservationRedis>) reservationRepository.findAll();
        List<ReservationRedis> reservationStartTimeList = null;

            if(userId != 0){
                reservationStartTimeList = list.stream()
                        .filter(reservationRedis -> reservationRedis != null && reservationRedis.getUserId() == userId)
                        .toList();
            }

        return reservationStartTimeList;
    }
}
