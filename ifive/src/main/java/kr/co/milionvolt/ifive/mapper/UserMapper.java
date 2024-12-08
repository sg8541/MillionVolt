package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.payment.UserInfoPaymentListVO;
import kr.co.milionvolt.ifive.domain.reservation.UserInfoReservationListVO;
import kr.co.milionvolt.ifive.domain.user.UserInfoDTO;
import kr.co.milionvolt.ifive.domain.user.UserVO;
import kr.co.milionvolt.ifive.domain.usercar.CarBatteryAndChargerTypeUpdateDTO;
import kr.co.milionvolt.ifive.domain.usercar.UserCarInfoDTO;
import kr.co.milionvolt.ifive.domain.usercar.CarNumberAndModelUpdateDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
    UserInfoDTO selectUser(Integer id);

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
    @Select("select car_number, car_battery, charger_type, model_name " +
            "from user_car " +
            "join charger_type " +
            "using (charger_type_id) " +
            "join car_model " +
            "using (model_id) " +
            "where user_car.car_id = #{carId}")
    UserCarInfoDTO findByUserCar(@Param("carId") Integer id);

    // 유저의 차 번호 + 차 모델 변경
    @Update("update user_car " +
            "set car_number = #{carNumber}, model_id = #{modelId} " +
            "where car_id = #{carId}")
    boolean updateUserCarNumberAndCarModel(CarNumberAndModelUpdateDTO carNumberDTO);

    // 유저의 차 배터리 + 선호 충전 타입 변경
    @Update("update user_car " +
            "set car_battery = #{carBattery}, charger_type_id = #{chargerTypeId} " +
            "where car_id = #{carId}")
    boolean updateUserCarBatteryAndChargerType(CarBatteryAndChargerTypeUpdateDTO updateDTO);

    // 유저의 예약 내역 리스트
    @Select("select reservation_id, start_time, end_time, status, r.created_at, charger_id, c.address " +
            "from reservation r " +
            "join charging_station c " +
            "using (station_id) " +
            "left join user " +
            "using (user_id) " +
            "where user_id = #{userId}")
    List<UserInfoReservationListVO> findByUserReservationList(@Param("userId") Integer id);

    // 유저의 결제 내역 리스트
    @Select("select payment_id, amount, payment_method, payment_status, p.created_at, " +
            "       charge_start, charge_end, c.address " +
            "from payment p " +
            "join charging_station c " +
            "using (station_id) " +
            "where user_id = #{userId}")
    List<UserInfoPaymentListVO> findByUserPaymentList(@Param("userId") Integer id);
}
