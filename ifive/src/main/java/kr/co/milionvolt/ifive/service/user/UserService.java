package kr.co.milionvolt.ifive.service.user;

import kr.co.milionvolt.ifive.domain.reservation.UserInfoReservationListDTO;
import kr.co.milionvolt.ifive.domain.user.PasswordDTO;
import kr.co.milionvolt.ifive.domain.user.UserInfoDTO;
import kr.co.milionvolt.ifive.domain.user.UserVO;
import kr.co.milionvolt.ifive.domain.usercar.CarBatteryAndChargerTypeUpdateDTO;
import kr.co.milionvolt.ifive.domain.usercar.UserCarInfoDTO;
import kr.co.milionvolt.ifive.domain.usercar.CarNumberAndModelUpdateDTO;

import java.util.List;

public interface UserService {
    // 유저 전체 조회
    List<UserVO> findAll();
    // 유저 정보 조회
    UserInfoDTO getUserInfo(Integer id);
    // 비밀번호 변경
    boolean updatePassword(Integer id, PasswordDTO passwordDTO);
    // 비밀번호 확인
    boolean findByPassword(Integer id, PasswordDTO passwordDTO);
    // 유저 차 조회
    UserCarInfoDTO userCarInfo(Integer id);
    // 유저 차 번호 + 모델 업데이트
    boolean updateUserCarNumberAndCarModel(CarNumberAndModelUpdateDTO carNumberDTO);
    // 유저 배터리 + 선호 타입 업데이트
    boolean updateUserCarBatteryAndChargerType(CarBatteryAndChargerTypeUpdateDTO updateDTO);
    // 유저의 예약리스트
    List<UserInfoReservationListDTO> getUserReservationList(Integer id);
}
