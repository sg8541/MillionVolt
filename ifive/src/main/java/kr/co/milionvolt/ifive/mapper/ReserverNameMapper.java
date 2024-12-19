package kr.co.milionvolt.ifive.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReserverNameMapper {
    @Select("SELECT username FROM user WHERE id = #{id}")
    String findUserId(int id);
}
