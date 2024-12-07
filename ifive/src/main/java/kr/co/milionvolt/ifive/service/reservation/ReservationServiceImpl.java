package kr.co.milionvolt.ifive.service.reservation;

import kr.co.milionvolt.ifive.domain.reservation.ReservationDTO;
import kr.co.milionvolt.ifive.mapper.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationMapper reservationMapper;

    @Autowired
    public ReservationServiceImpl(ReservationMapper reservationMapper){
        this.reservationMapper = reservationMapper;
    };

    @Override
    public boolean saveReservation(ReservationDTO reservationDTO){
        try {
            return reservationMapper.insertReservation(reservationDTO)>0;
        } catch (Exception e) {
            System.err.println("reservation error: " + e.getMessage());
            return false;
        }
    }
}
