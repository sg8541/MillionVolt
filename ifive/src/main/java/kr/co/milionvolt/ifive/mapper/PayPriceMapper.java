package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.payment.PayPriceDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayPriceMapper {

    @Insert("INSERT INTO payment " +
            "(payment_id, user_id, reservation_id, amount, payment_method, " +
            "payment_status, created_at, updated_at, charge_start, charge_end, imp_uid) " +
            "VALUES (#{paymentId}, #{userId}, #{reservationId}, #{amount}, #{paymentMethod}, " +
            "#{paymentStatus}, #{createdAt}, #{updatedAt}, #{chargeStart}, #{chargeEnd}, #{impUid})")
    int insertPayPrice(PayPriceDTO dto);
}

