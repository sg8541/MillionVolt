package kr.co.milionvolt.ifive.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CancelReservationMapper {

    @Select("SELECT imp_uid FROM reservation WHERE user_id = #{userId} AND reservation_id = #{reservationId}")
    String selectReservationImpUid(int userId, int reservationId);

    @Update("UPDATE reservation SET status = 'cancelled' WHERE imp_uid = #{impUid}")
    boolean updateReservationStatus(String impUid);
}
