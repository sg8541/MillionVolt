package kr.co.milionvolt.ifive.controller.reservation;

import kr.co.milionvolt.ifive.domain.reservation.ReservationListDTO;
import kr.co.milionvolt.ifive.service.reservation.ReservationListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
public class ReservationListController {

    @Autowired
    ReservationListServiceImpl reservationListServiceImpl;

    @GetMapping("/ReservationList/{startTime}/{endTime}")
    public List<ReservationListDTO> listReservations(
            @PathVariable Timestamp startTime,
            @PathVariable Timestamp endTime) {

        return reservationListServiceImpl.printReservationList(startTime, endTime);
    }
}
