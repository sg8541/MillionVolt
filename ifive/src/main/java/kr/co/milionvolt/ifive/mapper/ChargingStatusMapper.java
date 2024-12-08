package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.notification.ChargingStatusDTO;
import kr.co.milionvolt.ifive.domain.usercar.UserCarChargingUpdateDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ChargingStatusMapper {

    @Select(" SELECT u.id, u.user_id, u.username, uc.car_battery, uc.car_number, uc.car_id , " +
            " cm.model_id, cm.model_battery,r.reservation_id, r.start_time, r.end_time, " +
            " cs.station_id, cs.name, cs.address, " +
            " cs.price_per_kWh, ct.charger_type " +
            " FROM user u" +
            " JOIN user_car uc " +
            " ON u.id = uc.car_id " +
            " JOIN car_model cm " +
            " ON uc.car_id = cm.model_id " +
            " JOIN reservation r " +
            " ON u.id=r.user_id " +
            " JOIN charger c " +
            " ON r.reservation_id = c.charger_id " +
            " JOIN charging_station cs " +
            " ON cs.station_id = c.station_id " +
            " JOIN charger_type ct " +
            " ON ct.charger_type_id = c.charger_type_id " +
            " WHERE u.user_id = #{userId} " +
            " AND r.reservation_id = #{reservationId}")
    public ChargingStatusDTO chargingStatus(String userId, int reservationId);

    @Update( "UPDATE user_car SET  car_battery =#{carBattery} WHERE car_id=#{carId}")
    public void chargingUpdate(int carId, double carBattery);

}
