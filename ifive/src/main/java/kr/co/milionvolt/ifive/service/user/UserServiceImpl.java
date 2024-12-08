package kr.co.milionvolt.ifive.service.user;

import kr.co.milionvolt.ifive.domain.reservation.UserInfoReservationListVO;
import kr.co.milionvolt.ifive.domain.user.PasswordDTO;
import kr.co.milionvolt.ifive.domain.user.UserInfoDTO;
import kr.co.milionvolt.ifive.domain.usercar.CarBatteryAndChargerTypeUpdateDTO;
import kr.co.milionvolt.ifive.domain.usercar.UserCarInfoDTO;
import kr.co.milionvolt.ifive.domain.usercar.CarNumberAndModelUpdateDTO;
import kr.co.milionvolt.ifive.mapper.UserMapper;
import kr.co.milionvolt.ifive.domain.user.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // 유저 전체 조회
    @Override
    public List<UserVO> findAll() {
        List<UserVO> vo = new ArrayList<>();
        return vo;
    }

    // 유저 정보 조회
    @Override
    public UserInfoDTO getUserInfo(Integer id) {
        UserInfoDTO getUserInfo = userMapper.selectUser(id);
        log.info(getUserInfo.toString());
        return getUserInfo;
    }

    // 비밀번호 변경
    @Override
    public boolean updatePassword(Integer id, PasswordDTO passwordDTO) {
        boolean success = false;
        log.info(passwordDTO.toString());
        if(findByPassword(id, passwordDTO)){
            String newPassword = passwordEncoder.encode(passwordDTO.getNewPassword());
            log.info("newPassword: = ", newPassword);
            success = userMapper.updatePW(id, newPassword);
            return success;
            // 패스워드 변경 도중 실패한 상황에 대한 상태처리 추가 필요.
        }else{
            // 패스워드가 일치하지 않은 경우에 대한 값
            return false;
        }
    }

    // 비밀번호 확인
    @Override
    public boolean findByPassword(Integer id, PasswordDTO passwordDTO) {
        String currentPassword = userMapper.findByPassword(id);
        boolean findPasswordResult = passwordEncoder.matches(passwordDTO.getPassword(), currentPassword);
        return findPasswordResult;
    }

    // 차 정보 조회
    @Override
    public UserCarInfoDTO userCarInfo(Integer id) {
        UserCarInfoDTO carInfoDTO = userMapper.findByUserCar(id);
        return carInfoDTO;
    }
    // 차 번호 + 차 모델 변경
    @Override
    public boolean updateUserCarNumberAndCarModel(CarNumberAndModelUpdateDTO carNumberDTO) {
        boolean success = userMapper.updateUserCarNumberAndCarModel(carNumberDTO);
        return success;
    }
    // 차 배터리 + 선호 충전 타입 변경
    @Override
    public boolean updateUserCarBatteryAndChargerType(CarBatteryAndChargerTypeUpdateDTO updateDTO) {
        boolean success = userMapper.updateUserCarBatteryAndChargerType(updateDTO);
        return success;
    }

    @Override
    public List<UserInfoReservationListVO> getUserReservationList(Integer id) {
        List<UserInfoReservationListVO> reservationListDTO = userMapper.findByUserReservation(id);
        return reservationListDTO;
    }


}
