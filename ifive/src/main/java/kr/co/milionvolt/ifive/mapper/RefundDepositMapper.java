package kr.co.milionvolt.ifive.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RefundDepositMapper {

    //이용초과금 가져오기
    @Select("SELECT penalty_amount FROM penalty WHERE reservation_id = #{reservationId}")
    Integer selectPenaltyAmount(int reservationId);

    //예약테이블 imp_uid 가져오기
    @Select("SELECT imp_uid FROM reservation WHERE reservation_id = #{reservationId} AND status = 'completed'")
    String selectImpUid(int reservationId);

    @Update("UPDATE penalty SET refund_status = 'impossible' WHERE reservation_id = #{reservationId} AND refund_status = 'possible'")
    int updateRefundStatus(int reservationId);
}
