package kr.co.milionvolt.ifive.controller.user;

import kr.co.milionvolt.ifive.domain.reservation.UserInfoReservationListVO;
import kr.co.milionvolt.ifive.domain.user.PasswordDTO;
import kr.co.milionvolt.ifive.domain.user.UserInfoDTO;
import kr.co.milionvolt.ifive.domain.usercar.CarBatteryAndChargerTypeUpdateDTO;
import kr.co.milionvolt.ifive.domain.usercar.UserCarInfoDTO;
import kr.co.milionvolt.ifive.domain.usercar.CarNumberAndModelUpdateDTO;
import kr.co.milionvolt.ifive.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/info")
public class UserInfoController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> userInfo(@PathVariable Integer id) {
        UserInfoDTO infoDTO = userService.getUserInfo(id);
        log.info(infoDTO.toString());
        if (infoDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원조회 실패");
        }
        return ResponseEntity.status(HttpStatus.OK).body(infoDTO);
    }

    // 비밀번호 변경
    @PutMapping("/{id}")
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
    @PutMapping("/car/{id}")
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
    public ResponseEntity<?> updateUserCarBatteryAndChargerType(@PathVariable Integer id,  @RequestBody CarBatteryAndChargerTypeUpdateDTO updateDTO) {
        updateDTO.setCarId(id);
        boolean success = userService.updateUserCarBatteryAndChargerType(updateDTO);
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
        return ResponseEntity.status(HttpStatus.OK).body(reservationListDTO);
    }



}
