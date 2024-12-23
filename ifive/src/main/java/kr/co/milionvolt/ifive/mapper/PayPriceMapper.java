package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.payment.PayPriceDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayPriceMapper {

    @Insert("INSERT INTO payment " +
            "(user_id, reservation_id, station_id, amount, charged_energy, payment_method, " +
            "payment_status, created_at, updated_at, charge_start, charge_end, imp_uid) " +
            "VALUES (#{userId}, #{reservationId}, #{stationId}, #{amount}, #{chargedEnergy},#{paymentMethod}, " +
            "#{paymentStatus}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, #{chargeStart}, #{chargeEnd}, #{impUid})")
    boolean insertPayPrice(PayPriceDTO dto);
}