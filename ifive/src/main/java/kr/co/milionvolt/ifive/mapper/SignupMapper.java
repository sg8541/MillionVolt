package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.user.SignupDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SignupMapper {
    @Insert("insert into user(username, user_id, email, phone_number, password) " +
            "values (#{username}, #{userId}, #{email}, #{phoneNumber}, #{password})")
    boolean insertUser(SignupDTO signupDTO);


}
