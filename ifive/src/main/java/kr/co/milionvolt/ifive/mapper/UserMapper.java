package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.user.UserInfoDTO;
import kr.co.milionvolt.ifive.domain.user.UserVO;
import kr.co.milionvolt.ifive.domain.usercar.UserCarInfoDTO;
import lombok.Value;
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
}
