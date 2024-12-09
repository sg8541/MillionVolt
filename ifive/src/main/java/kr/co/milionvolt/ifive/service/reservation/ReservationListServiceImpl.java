package kr.co.milionvolt.ifive.service.reservation;

import kr.co.milionvolt.ifive.domain.reservation.ReservationListDTO;
import kr.co.milionvolt.ifive.mapper.ReservationListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ReservationListServiceImpl implements ReservationListService {

    @Autowired
    private ReservationListMapper reservationListMapper;

    @Override
    public List<ReservationListDTO> printReservationList(Timestamp startTime, Timestamp endTime) {
        return reservationListMapper.selectReservationList(startTime, endTime);
    }
}

