package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.notification.ChargingStatusDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ChargingStatusMapper {

    @Select(" SELECT u.email, u.username , uc.car_battery, " +
            " cm.model_battery, r.start_time, r.end_time, " +
            " cs.station_id, cs.name, cs.address, " +
            " cs.price_per_kWh, c.charger_type " +
            " FROM user u" +
            " JOIN user_car uc " +
            " ON u.id = uc.car_id " +
            " JOIN car_model cm " +
            " ON uc.car_id = cm.model_id " +
            " JOIN reservation r " +
            " ON u.id=r.reservation_id " +
            " JOIN charger c " +
            " ON r.reservation_id = c.charger_id " +
            " JOIN charging_station cs " +
            " ON cs.station_id = c.station_id " +
            " WHERE u.user_id = #{userId} " +
            " AND r.reservation_id = #{reservationId}")
    public ChargingStatusDTO ChargingStatus(String userId, int reservationId);

}
