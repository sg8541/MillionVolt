package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.user.*;
import kr.co.milionvolt.ifive.domain.userinfo.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    // 유저 전체 조회
    @Select("select * from user where user_id > 0 ")
    List<UserVO> getList();

    // 유저 정보 조회
    @Select("select username, user_id, email, phone_number " +
            "from user " +
            "where id = #{id}")
    UserInfoDTO selectId(Integer id);
    // 유저 정보 조회
    @Select("select username, user_id, email, phone_number " +
            "from user " +
            "where user_id = #{userId}")
    UserInfoDTO selectUser(String userId);

    // 유저 비밀번호 변경
    @Update("update user " +
            "set password = #{newPassword} where id = #{id}")
    boolean updatePW(Integer id, String newPassword);

    // 유저 비밀번호 찾기
    @Select("select password " +
            "from user " +
            "where id = #{id}")
    String findByPassword(Integer id);

    // 유저의 자동차 정보 조회
    @Select("select car_number, car_battery, charger_speed_id, model_id,  model_battery " +
            "from user_car " +
            "join charge_speed " +
            "using (charger_speed_id) " +
            "join car_model " +
            "using (model_id) " +
            "where user_car.car_id = #{carId}")
    UserCarInfoDTO findByUserCar(@Param("carId") Integer id);

    // 유저의 차 번호 + 차 모델 차 배터리 + 선호 충전 타입 변경
    @Update("update user_car " +
            "set " +
            "car_battery = #{carBattery}, " +
            "charger_speed_id = #{chargerSpeedId}, " +
            "car_number = #{carNumber}, " +
            "model_id = #{modelId} " +
            "where car_id = #{carId}")
    boolean updateUserCarInfo(CarInfoUpdateDTO updateDTO);

    // 유저의 예약 내역 리스트
    @Select("select reservation_id, " +
            "date_format(start_time, '%y.%m.%d. %T ') as start_time, " +
            "date_format(end_time, ' %y.%m.%d. %T') as end_time, " +
            "status, " +
            "date_format(r.created_at,'%y.%m.%d.') as  created_at," +
            "charger_id, " +
            "c.name " +
            "from reservation r " +
            "join charging_station c " +
            "using (station_id) " +
            "left join user " +
            "using (user_id) " +
            "where user_id = #{userId} " +
            "order by r.created_at desc ")
    List<UserInfoReservationListVO> findByUserReservationList(@Param("userId") Integer id);

    // 유저의 결제 내역 리스트
    @Select("select payment_id, name, cg.charger_id, format(amount,2) as amount, " +
            "       payment_method, payment_status, charged_energy, " +
            "       date_format(p.updated_at, '%y.%m.%d.') as updated_at, " +
            "       date_format(p.created_at, '%y.%m.%d. %H:%i') as created_at, " +
            "       date_format(charge_start, '%y.%m.%d. %T ') as charge_start, " +
            "       date_format(charge_end, ' %y.%m.%d. %T') as charge_end " +
            "from payment p " +
            "join charging_station c " +
            "using (station_id) " +
            "join charger cg " +
            "on c.station_id = cg.charger_id " +
            "where user_id = #{userId} " +
            "order by p.created_at desc ")
    List<UserInfoPaymentListVO> findByUserPaymentList(@Param("userId") Integer id);

    // 대시보드 유저 + 유저 차 정보 + 차 모델 + 선호 충전 타입
    @Select("select username, " +
            "       car_battery,  " +
            "       model_name, model_battery, model_filepath, " +
            "       charger_speed " +
            "from user_car " +
            "JOIN user u ON car_id = u.id " +
            "join car_model " +
            "using (model_id) " +
            "join charge_speed " +
            "using (charger_speed_id) " +
            "where car_id = #{carId}")
    UserDashboradUserCarDTO findByUserCarAndCarModel(@Param("carId") Integer id);

    // 대시보드 예약리스트 최대 5개 불러옴
    @Select("select reservation_id, " +
            "date_format(start_time, '%y.%m.%d. %T ') as start_time, " +
            "date_format(end_time, ' %y.%m.%d. %T') as end_time, " +
            "status, " +
            "date_format(r.created_at,'%y.%m.%d.') as  created_at," +
            "charger_id, " +
            "c.name " +
            "from reservation r " +
            "join charging_station c " +
            "using (station_id) " +
            "left join user " +
            "using (user_id) " +
            "where user_id = #{userId} " +
            "order by r.created_at desc " +
            "limit 5")
    List<UserInfoReservationListVO> getDashboardReservations(Integer id);

    // 유저의 대시보드 결제 내역 리스트
    @Select("select payment_id, name, cg.charger_id, format(amount,2) as amount, " +
            "       payment_method, payment_status, charged_energy, " +
            "       date_format(p.updated_at, '%y.%m.%d.') as updated_at, " +
            "       date_format(p.created_at, '%y.%m.%d. %H:%i') as created_at, " +
            "       date_format(charge_start, '%y.%m.%d. %T ') as charge_start, " +
            "       date_format(charge_end, ' %T') as charge_end " +
            "from payment p " +
            "join charging_station c " +
            "using (station_id) " +
            "join charger cg " +
            "on c.station_id = cg.charger_id " +
            "where user_id = #{userId} " +
            "order by p.created_at desc " +
            "limit 5")
    List<UserInfoPaymentListVO> getDashboardPayments(Integer id);


    @Select("SELECT " +
            "    u.id, " +
            "    u.user_id, " +
            "    u.password, " +
            "    u.username, " +
            "    u.role, " +
            "    uc.car_battery, " +
            "    cm.model_battery " +
            "FROM user_car uc " +
            "JOIN user u ON uc.car_id = u.id " +
            "JOIN car_model cm ON uc.model_id = cm.model_id " +
            "where user_id = #{userId}")
    UserDetailsVO findByUserId(String userId);

    @Select("SELECT " +
            "    u.id, " +
            "    u.user_id, " +
            "    u.password, " +
            "    u.username, " +
            "    u.role, " +
            "    uc.car_battery, " +
            "    cm.model_battery " +
            "FROM user_car uc " +
            "JOIN user u ON uc.car_id = u.id " +
            "JOIN car_model cm ON uc.model_id = cm.model_id " +
            "where id = #{id}")
    UserDetailsVO findById(Integer id);


    //아이디찾기
    @Select(" SELECT user_id, created_at FROM user " +
            " WHERE username = #{username} " +
            " AND email = #{email} ")
    public FindIdDTO findByIdUserId(String username, String email);

    // 1. 비밀번호 찾기
    @Select(" SELECT email FROM user " +
            " WHERE user_id = #{userId}")
    public String findPasswordByUserId(String userId);

    // 2. 비밀번호 찾기
    @Select(" SELECT username, user_id, email, password " +
            " FROM user " +
            " WHERE username = #{username} " +
            " AND email = #{email} ")
    public FindFwdDTO findPassword(String username , String email);

    // 3. 비밀번호 찾기
    @Update(" UPDATE user SET password = #{password}" +
            " WHERE user_id = #{userId} " +
            " AND username = #{username} " +
            " AND email = #{email} ")
    public void newPassword(ResetDTO dto);

    @Select("select amount, date_format(updated_at, '%m.%d.') as updated_at " +
            "from payment " +
            "where user_id = #{userId} and payment_status = 'completed' " +
            "order by updated_at desc " +
            "limit 7")
    List<UserDashboradChartDTO> getDashboardChartData(Integer userId);

    @Update("update user " +
            "set username = #{username}, email=#{email}, phone_number = #{phoneNumber} " +
            "where id = #{id}")
    boolean updateUserInfo(UpdateUserInfoDTO infoDTO);

    @Select(" SELECT id FROM user where user_id=#{userId}")
    public Integer selectFindId(String userId);

    @Update("update user_car " +
            "set car_battery = #{carBattery} " +
            "where car_id = #{carId}")
    boolean updateUserCarBattery(Integer carId, String carBattery);

    @Delete("delete from user where id = #{id}")
    boolean exitUser(Integer id);
}
