package kr.co.milionvolt.ifive.controller.user;

import kr.co.milionvolt.ifive.domain.userinfo.*;
import kr.co.milionvolt.ifive.domain.user.PasswordDTO;
import kr.co.milionvolt.ifive.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/myinfo")
public class UserInfoController {

    private final UserService userService;
    private final int PAYMENT_LIST_NO_DATA = 0;
    private final int RESERVATION_LIST_NO_DATA = 0;



    @GetMapping("/{id}")
    public ResponseEntity<?> userInfo(@PathVariable Integer id) {
        UserInfoDTO infoDTO = userService.getUserInfo(id);
        log.info(infoDTO.toString());
        if (infoDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원조회 실패");
        }
        return ResponseEntity.status(HttpStatus.OK).body(infoDTO);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> userInfoUpdate(@PathVariable Integer id, @RequestBody UpdateUserInfoDTO infoDTO) {
        infoDTO.setId(id);
        boolean success = userService.updateUserInfo(infoDTO);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK).body(infoDTO);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원정보 변경에 실패하였습니다.");
        }
    }

    // 비밀번호 변경
    @PostMapping("/password/{id}")
    public ResponseEntity<?> userPWUpdate(@PathVariable Integer id, @RequestBody PasswordDTO passwordDTO) {
        boolean success = userService.updatePassword(id, passwordDTO);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK).body("비밀번호가 성공적으로 변경되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("기존 비밀번호가 일치하지 않습니다.");
        }
    }

    // 내 차 정보 조회
    @GetMapping("/car/{id}")
    public ResponseEntity<?> userCarInfo(@PathVariable Integer id) {
        try {
            UserCarInfoDTO userCarInfo = userService.userCarInfo(id);
            return ResponseEntity.status(HttpStatus.OK).body(userCarInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 내 차 수정(차 번호 + 차 넘버 변경)
    @PostMapping("/car/{id}")
    public ResponseEntity<?> updateUserCarNumberAndCarModel(@PathVariable Integer id, @RequestBody CarNumberAndModelUpdateDTO carNumberDTO) {
        carNumberDTO.setCarId(id);
        boolean success = userService.updateUserCarNumberAndCarModel(carNumberDTO);
        if(success) {
            return ResponseEntity.status(HttpStatus.OK).body("차 번호와 모델 정보가 성공적으로 변경되었습니다.");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("정보 수정 중 문제가 발생하였습니다.");
        }
    }

    // 내 차 수정(차 배터리 + 선호 kW 정보)
    @PatchMapping("/car/{id}")
    public ResponseEntity<?> updateUserCarBatteryAndChargerType(@PathVariable Integer id,  @RequestBody CarBatteryAndChargerSpeedUpdateDTO updateDTO) {
        updateDTO.setCarId(id);
        boolean success = userService.updateUserCarBatteryAndChargerType(updateDTO);
        if(success) {
            return ResponseEntity.status(HttpStatus.OK).body("배터리와 충전 선호타입이 성공적으로 변경되었습니다.");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("정보 수정 중 문제가 발생하였습니다.");
        }
    }

    // 내 차 수정(차 배터리)
    @PutMapping("/car/{id}")
    public ResponseEntity<?> updateUserCarBatteryAndChargerType(@PathVariable Integer id,
                                                                @RequestBody Map<String, String> request) {

        String carBattery = request.get("carBattery");
        boolean success = userService.updateUserCarBattery(id, carBattery);
        if(success) {
            return ResponseEntity.status(HttpStatus.OK).body("배터리와 충전 선호타입이 성공적으로 변경되었습니다.");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("정보 수정 중 문제가 발생하였습니다.");
        }
    }



    // 예약 리스트 조회
    @GetMapping("/reservation/{id}")
    public ResponseEntity<?> getReservationList(@PathVariable Integer id) {
        List<UserInfoReservationListVO> reservationListDTO = userService.getUserReservationList(id);
        log.info(reservationListDTO.toString());
        if(RESERVATION_LIST_NO_DATA == reservationListDTO.size()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("예약 내역이 존재하지 않습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(reservationListDTO);
    }

    // 결제 리스트 조회
    @GetMapping("/payment/{id}")
    public ResponseEntity<?> getPaymentList(@PathVariable Integer id) {
        List<UserInfoPaymentListVO> paymentListDTO = userService.getUserPaymentList(id);
        log.info(paymentListDTO.toString());
        if(PAYMENT_LIST_NO_DATA == paymentListDTO.size()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 내역이 존재하지 않습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(paymentListDTO);
    }

    // 대시보드 (유저 이름 + 차 정보 + 예약 및 결제내역) 추신 : 충전량 차트 정보 못 불러옴
    @GetMapping("/dashboard/{id}")
    public ResponseEntity<?> getDashborad(@PathVariable Integer id) {
        UserDashboradUserCarDTO dashboradUserCarDTO = userService.getDashboardUserCarInfo(id);
        List<UserInfoReservationListVO> reservationListDTO = userService.getDashboardReservations(id);
        List<UserInfoPaymentListVO> paymentListDTO = userService.getDashboardPayments(id);
        List<UserDashboradChartDTO> dashboradChartDTO = userService.getDashboardChart(id);

        DashboardResponseDTO dashboardResponseDTO = new DashboardResponseDTO();
        dashboardResponseDTO.setUserCarInfo(dashboradUserCarDTO);
        dashboardResponseDTO.setReservationList(reservationListDTO);
        dashboardResponseDTO.setPaymentList(paymentListDTO);
        dashboardResponseDTO.setPaymentChartList(dashboradChartDTO);
        return ResponseEntity.status(HttpStatus.OK).body(dashboardResponseDTO);
    }




}
