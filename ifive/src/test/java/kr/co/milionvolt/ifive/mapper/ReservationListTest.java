package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.reservation.ReservationListDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ReservationListTest {
    @Autowired
    ReservationListMapper reservationListMapper;

    @Test
    public void test() {
        ReservationListDTO reservationListDTO = new ReservationListDTO();

        List<ReservationListDTO> dtoList = reservationListMapper.selectReservationList(reservationListDTO.getStartTime(), reservationListDTO.getEndTime(), reservationListDTO.getStationId(), reservationListDTO.getReservationId());
        System.out.println("성공" + dtoList);
    }
}
