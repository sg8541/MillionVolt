package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.dto.PayPriceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PayPriceMapper {

    @Select("SELECT " +
            "u.username AS reserverName, " +
            "p.amount AS expectedAmount, " +
            "pen.penalty_amount AS deposit, " +
            "c.charger_status AS chargerType, " +
            "CONCAT(c.charger_type, ' ', cs.name) AS chargerNumber, " +
            "CONCAT(DATE_FORMAT(r.created_at, '%Y%m%d%H%i%S'), '-', r.reservation_id) AS reservationCode " +
            "FROM reservation r " +
            "JOIN charger c ON r.charger_id = c.charger_id " +
            "JOIN user u ON r.reservation_id = u.id " +
            "LEFT JOIN charging_station cs ON c.charger_id = cs.station_id " +
            "LEFT JOIN payment p ON r.reservation_id = p.reservation_id " +
            "LEFT JOIN penalty pen ON r.reservation_id = pen.penalty_id " +
            "WHERE u.user_id = #{user_id}")
    PayPriceDTO getPayPriceDetails(int user_id);  // 파라미터로 user_id 받기
}
