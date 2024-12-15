package kr.co.milionvolt.ifive.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CancelPaymentMapper {
    @Select("SELECT imp_uid FROM payment WHERE user_id = #{userId} AND payment_id = #{paymentId}")
    String selectImpUid(int userId, int paymentId);

    @Update("UPDATE payment SET payment_status = 'cancelled' WHERE imp_uid = #{impUid}")
    boolean updateStatus(String impUid);
    }
