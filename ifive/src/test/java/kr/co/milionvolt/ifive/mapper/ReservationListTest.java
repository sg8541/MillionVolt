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
       List<ReservationListDTO> dtoList = reservationListMapper.printReservations("2024-12-07", "2024-12-08");
        System.out.println("성공" + dtoList);
    }
}
