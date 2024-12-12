package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.user.SignupDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

@Mapper
public interface SignupMapper {
    @Insert("insert into user(username, user_id, email, phone_number, password) " +
            "values (#{username}, #{userId}, #{email}, #{phoneNumber}, #{password})")
    boolean insertUser(SignupDTO signupDTO);

    @Insert("insert into user_car(car_id, car_number, model_id, charger_speed_id) " +
            "values (#{carId}, #{carNumber}, #{modelId}, #{chargerSpeedId})")
    boolean insertUserCar(String carId, String carNumber, Integer chargerSpeedId, Integer modelId, BigDecimal carBattery);


    @Select("select id from user where email = #{email}")
    String findById(String email);


}
