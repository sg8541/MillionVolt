package kr.co.milionvolt.ifive.service.user;

import kr.co.milionvolt.ifive.domain.user.*;
import kr.co.milionvolt.ifive.domain.userinfo.*;

import java.util.List;

public interface UserService {
    // 유저 전체 조회
    List<UserVO> findAll();
    // 유저 정보 조회
    UserInfoDTO getUserInfo(Integer id);
    // 비밀번호 변경
    boolean updatePassword(Integer id, PasswordDTO passwordDTO);
    // 비밀번호 확인
    boolean findByPassword(Integer id, String password);
    // 유저 차 조회
    UserCarInfoDTO userCarInfo(Integer id);
    // 유저 차 정보 업데이트
    boolean updateUserCarInfo(CarInfoUpdateDTO updateDTO);
    // 유저의 예약 내역
    List<UserInfoReservationListVO> getUserReservationList(Integer id);
    // 유저의 결제 내역
    List<UserInfoPaymentListVO> getUserPaymentList(Integer id);
    // 유저 대시보드 차 + 선호충전타입 정보
    UserDashboradUserCarDTO getDashboardUserCarInfo(Integer id);
    // 유저 대시보드 예약 리스트
    List<UserInfoReservationListVO> getDashboardReservations(Integer id);
    // 유저 대시보드 결제 리스트
    List<UserInfoPaymentListVO> getDashboardPayments(Integer id);


    public FindIdDTO findByID(String username, String email); // 아이디 찾기
    public String findPasswordByUserId(String userId); // 비밀번호 찾기1
    public FindFwdDTO findPass(String username, String email); // 비밀번호 찾기2
    public void newPwd(ResetDTO dto);// 비밀번호 초기화

    List<UserDashboradChartDTO> getDashboardChart(Integer id);

    boolean updateUserInfo(UpdateUserInfoDTO infoDTO);
    public int selectUserId(String userId); // 예약번호 조회 알림때 사용하는 메서드

    boolean updateUserCarBattery(Integer id, String carBettery);

    int exitUser(Integer id, String password);
}
