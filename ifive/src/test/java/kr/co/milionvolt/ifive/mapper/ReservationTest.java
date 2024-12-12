//package kr.co.milionvolt.ifive.mapper;
//
//import kr.co.milionvolt.ifive.domain.reservation.ReservationDTO;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//
//@SpringBootTest
//public class ReservationTest {
//
//    @Autowired
//    private ReservationMapper reservationMapper;
//
//    @Test
//    public void insertReservation() {
//        // DTO 객체 생성
//        ReservationDTO reservationDTO = new ReservationDTO();
//        Timestamp creatTime = Timestamp.valueOf(LocalDateTime.now());
//        Timestamp startTime = Timestamp.valueOf(LocalDateTime.of(2024, 12, 7, 12, 0));
//        Timestamp endTime = Timestamp.valueOf(LocalDateTime.of(2024, 12, 7, 14, 15));
//
//        reservationDTO.setReservationId(4);
//        reservationDTO.setStartTime(startTime);
//        reservationDTO.setEndTime(endTime);
//        reservationDTO.setStatus(ReservationDTO.Status.pending); // Enum 값 설정
//        reservationDTO.setCreatedAt(creatTime);
//        reservationDTO.setChargerId(3);
//        reservationDTO.setUserId(3);
//        reservationDTO.setStationId(3);
//
//        // 데이터 삽입 테스트
//        int result = reservationMapper.insertReservation(reservationDTO);
//
//        // 결과 출력
//        System.out.println("삽입 결과: " + result);
//    }
//}
//
