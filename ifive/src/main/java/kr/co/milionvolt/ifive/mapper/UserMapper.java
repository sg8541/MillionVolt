package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    // 유저 전체 조회
    @Select("select * from user where user_id > 0 ")
    List<UserVO> getList();
    @Select(" SELECT user_id, created_at FROM user " +
            " WHERE username = #{username} " +
            " AND email = #{email} ")
    public UserVO findById(String username, String email);

    @Select(" SELECT email FROM user " +
            " WHERE user_id = #{userId}")
    public String findPasswordByUserId(String userId);

    @Select(" SELECT username, user_id, email, password " +
            " FROM user " +
            " WHERE username = #{username} " +
            " AND email = #{email} ")
    public UserVO findPassword(String username , String email);

    @Update(" UPDATE user SET password = #{password}" +
            " WHERE user_id = #{user_id} " +
            " AND username = #{username} " +
            " AND email = #{email} ")
    public void newPassword(UserVO vo);

}
