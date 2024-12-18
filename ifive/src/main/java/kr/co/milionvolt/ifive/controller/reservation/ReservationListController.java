//package kr.co.milionvolt.ifive.controller.reservation;
//
//import kr.co.milionvolt.ifive.domain.reservation.ReservationListDTO;
//import kr.co.milionvolt.ifive.service.reservation.ReservationListServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.sql.Timestamp;
//import java.util.List;
//
//@RestController
//public class ReservationListController {
//
//    @Autowired
//    ReservationListServiceImpl reservationListServiceImpl;
//
//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    @GetMapping("/reservationList/{startTime}/{endTime}")
//    public List<ReservationListDTO> listReservations(
//            @PathVariable String startTime,
//            @PathVariable String endTime) throws ParseException {
//
//        Timestamp startTimestamp = new Timestamp(dateFormat.parse(startTime).getTime());
//        Timestamp endTimestamp = new Timestamp(dateFormat.parse(endTime).getTime());
//        System.out.println(startTime);
//        System.out.println(endTime);
//
//        return reservationListServiceImpl.printReservationList(startTimestamp, endTimestamp);
//    }
//}

package kr.co.milionvolt.ifive.controller.reservation;

import kr.co.milionvolt.ifive.domain.reservation.ReservationListDTO;
import kr.co.milionvolt.ifive.service.reservation.ReservationListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ReservationListController {

    @Autowired
    private ReservationListServiceImpl reservationListServiceImpl;

    @GetMapping("/reservationList/{startTime}/{endTime}/{stationId}/{chargerId}")
    public List<ReservationListDTO> listReservations(
            @PathVariable String startTime,
            @PathVariable String  endTime,
            @PathVariable int stationId,
            @PathVariable int chargerId) {

        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);

        LocalDateTime startDateTime = LocalDateTime.parse(startTime);
        LocalDateTime endDateTime = LocalDateTime.parse(endTime);

        return reservationListServiceImpl.printReservationList(startDateTime, endDateTime, stationId, chargerId);
    }
}

