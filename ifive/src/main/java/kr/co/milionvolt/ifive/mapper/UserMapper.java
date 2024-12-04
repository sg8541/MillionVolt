package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface UserMapper {

    // 유저 전체 조회
    @Select("select * from user where user_id > 0 ")
    List<UserVO> getList();

}
