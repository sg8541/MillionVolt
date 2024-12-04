package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.dto.PayPriceDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PayPriceMapper {

    @Insert("INSERT INTO payment " +
            "(payment_id, user_id, reservation_id, amount, payment_method, " +
            "payment_status, created_at, updated_at, charge_start, charge_end) " +
            "VALUES (#{paymentId}, #{userId}, #{reservationId}, #{amount}, #{paymentMethod}, " +
            "#{paymentStatus}, #{createdAt}, #{updatedAt}, #{chargeStart}, #{chargeEnd})")
    int insertPayPrice(PayPriceDTO dto);
}

