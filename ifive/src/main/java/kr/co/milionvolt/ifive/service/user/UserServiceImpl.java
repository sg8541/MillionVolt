package kr.co.milionvolt.ifive.service.user;

import kr.co.milionvolt.ifive.domain.user.*;
import kr.co.milionvolt.ifive.domain.userinfo.*;
import kr.co.milionvolt.ifive.mapper.UserMapper;
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
        UserInfoDTO getUserInfo = userMapper.selectId(id);
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
    public boolean updateUserCarBatteryAndChargerType(CarBatteryAndChargerSpeedUpdateDTO updateDTO) {
        boolean success = userMapper.updateUserCarBatteryAndChargerSpeed(updateDTO);
        return success;
    }

    // 유저의 예약 내역 리스트
    @Override
    public List<UserInfoReservationListVO> getUserReservationList(Integer id) {
        List<UserInfoReservationListVO> reservationListDTO = userMapper.findByUserReservationList(id);
        return reservationListDTO;
    }

    // 유저의 결제 내역 리스트
    @Override
    public List<UserInfoPaymentListVO> getUserPaymentList(Integer id) {
        List<UserInfoPaymentListVO> paymentListVO = userMapper.findByUserPaymentList(id);
        return paymentListVO;
    }

    // 유저 대시보드 유저 이름 + 차 정보
    @Override
    public UserDashboradUserCarDTO getDashboardUserCarInfo(Integer id) {
        UserDashboradUserCarDTO dashboradUserCarDTO = userMapper.findByUserCarAndCarModel(id);
        return dashboradUserCarDTO;
    }

    // 유저 대시보드 예약 리스트 최대 5개
    @Override
    public List<UserInfoReservationListVO> getDashboardReservations(Integer id) {
        List<UserInfoReservationListVO> reservationListDTO = userMapper.getDashboardReservations(id);
        return reservationListDTO;
    }

    // 유저 대시보드 결제 리스트 최대 5개
    @Override
    public List<UserInfoPaymentListVO> getDashboardPayments(Integer id) {
        List<UserInfoPaymentListVO> paymentListVO = userMapper.getDashboardPayments(id);
        return paymentListVO;
    }


    //아이디 찾기
    @Override
    public FindIdDTO findByID(String username, String email) {
        FindIdDTO dto = userMapper.findByIdUserId(username, email);
        return dto;
    }

    //비밀번호 찾기1
    @Override
    public String findPasswordByUserId(String userId) {
        String  email = userMapper.findPasswordByUserId(userId);
        return email;
    }


    //비밀번호 찾기2
    @Override
    public FindFwdDTO findPass(String username, String email) {
        FindFwdDTO dto =  userMapper.findPassword(username,email);
        return dto;
    }

    //비밀번호 찾기3
    @Override
    public void newPwd(ResetDTO dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        userMapper.newPassword(dto);
    }

    @Override
    public List<UserDashboradChartDTO> getDashboardChart(Integer id) {
        List<UserDashboradChartDTO> dashboradChartDTO = userMapper.getDashboardChartData(id);
        return dashboradChartDTO;
    }

    @Override
    public boolean updateUserInfo(UpdateUserInfoDTO infoDTO) {
        try{
            boolean result = userMapper.updateUserInfo(infoDTO);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int selectUserId(String userId) {
        int id =   userMapper.selectFindId(userId);
        return id;
    }

    @Override
    public boolean updateUserCarBattery(Integer id, String carBettery) {
        boolean result = userMapper.updateUserCarBattery(id, carBettery);
        return result;
    }

}
