package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChargingStationMapper {

    @Select("SELECT cs.station_id, cs.name, cs.address, cs.total_charger, cs.available_charger, " +
            "cs.charger_7kW_count, cs.charger_50kW_Count, cs.charger_100kW_Count, " +
            "cs.charger_200kW_count, cs.charger_300kW_Plus_Count, cs.charge_speed, cs.price_per_kwh, " +
            "cs.created_at, cs.file_path " +
            "FROM charging_station cs")
    List<ChargingStationVO> getAllChargingStationList();

    @Select("SELECT cs.station_id, cs.name, cs.address, cs.total_charger, cs.available_charger, " +
            "cs.charger_7kW_Count, cs.charger_50kW_Count, cs.charger_100kW_Count, " +
            "cs.charger_200kW_Count, cs.charger_300kW_Plus_Count, cs.charge_speed, cs.price_per_kwh, " +
            "cs.created_at, cs.file_path, cs.business_info, cs.restrictions, cs.operating_hours, cs.phone_number, " +
            "c.charger_status " +
            "FROM charging_station cs " +
            "LEFT JOIN charger c ON cs.station_id = c.station_id " +
            "WHERE cs.station_id = #{stationId}")
    ChargingStationVO getOneChargingStation(@Param("stationId") Integer stationId);

    @Update("UPDATE charging_station SET name = #{name}, address = #{address}, total_charger = #{totalCharger}, " +
            "available_charger = #{availableCharger}, charger_7kW_Count = #{charger_7kW_Count}, " +
            "charger_50kW_Count = #{charger_50kW_Count}, charger_100kW_Count = #{charger_100kW_Count}, " +
            "charger_200kW_Count = #{charger_200kW_Count}, charger_300kW_Plus_Count = #{charger_300kW_Plus_Count}, " +
            "charge_speed = #{chargeSpeed}, price_per_kwh = #{pricePerKWh}, file_path = #{filePath} " +
            "WHERE station_id = #{stationId}")
    int updateChargingStation(ChargingStationDTO chargingStationDTO);

    @Update("UPDATE charger SET charger_type = #{chargerType}, charger_status = #{chargerStatus} " +
            "WHERE charger_id = #{chargerId}")
    int updateChargerInfo(@Param("chargerId") Integer chargerId,
                          @Param("chargerType") ChargerDTO.ChagerType chargerType,
                          @Param("chargerStatus") ChargerDTO.ChargerStatus chargerStatus);

    @Select("SELECT cs.station_id, cs.name, cs.address, cs.total_charger, cs.available_charger, " +
            "cs.charger_7kW_Count, cs.charger_50kW_Count, cs.charger_100kW_Count, " +
            "cs.charger_200kW_Count, cs.charger_300kW_Plus_Count, cs.charge_speed, cs.price_per_kwh, " +
            "cs.created_at, cs.file_path " +
            "FROM charging_station cs " +
            "WHERE (#{chargeSpeed} IS NULL OR cs.charge_speed = #{chargeSpeed}) AND " +
            "(#{chargerStatus} IS NULL OR cs.station_id IN (SELECT station_id FROM charger WHERE charger_status = #{chargerStatus}))")
    List<ChargingStationVO> getChargingStationsByFilter(@Param("chargeSpeed") ChargingStationDTO.ChargeSpeed chargeSpeed,
                                                        @Param("chargerStatus") ChargerDTO.ChargerStatus chargerStatus);

    @Select("SELECT cs.station_id, cs.name, cs.address, cs.total_charger, cs.available_charger, " +
            "cs.charger_7kW_Count, cs.charger_50kW_Count, cs.charger_100kW_Count, " +
            "cs.charger_200kW_Count, cs.charger_300kW_Plus_Count, cs.charge_speed, cs.price_per_kwh, " +
            "cs.created_at, cs.file_path " +
            "FROM charging_station cs " +
            "WHERE cs.address = #{address}")
    ChargingStationVO getChargingStationByAddress(@Param("address") String address);

    @Select("SELECT cs.station_id, cs.name, cs.address, cs.total_charger, cs.available_charger, " +
            "cs.charger_7kW_Count, cs.charger_50kW_Count, cs.charger_100kW_Count, " +
            "cs.charger_200kW_Count, cs.charger_300kW_Plus_Count, cs.charge_speed, cs.price_per_kwh, " +
            "cs.created_at, cs.file_path " +
            "FROM charging_station cs " +
            "WHERE (#{chargeSpeed} IS NULL OR cs.charge_speed = #{chargeSpeed}) " +
            "AND (#{chargerStatus} IS NULL OR cs.station_id IN " +
            "(SELECT station_id FROM charger WHERE charger_status = #{chargerStatus})) " +
            "AND (#{address} IS NULL OR cs.address LIKE CONCAT('%', #{address}, '%'))")
    List<ChargingStationVO> getChargingStationsByLocation(@Param("address") String address,
                                                          @Param("chargeSpeed") String chargeSpeed,
                                                          @Param("chargerStatus") String chargerStatus);

    @Select("SELECT cs.station_id, cs.name, cs.address, cs.total_charger, cs.available_charger, " +
            "cs.charger_7kW_Count, cs.charger_50kW_Count, cs.charger_100kW_Count, " +
            "cs.charger_200kW_Count, cs.charger_300kW_Plus_Count, cs.charge_speed, cs.price_per_kwh, " +
            "cs.created_at, cs.file_path " +
            "FROM charging_station cs " +
            "WHERE cs.name LIKE CONCAT('%', #{query}, '%')")
    List<ChargingStationVO> searchChargingStations(@Param("query") String query);


    @Insert("INSERT INTO charging_station (station_id, name, address, total_charger, available_charger, " +
            "charger_7kW_Count, charger_50kW_Count, charger_100kW_Count, " +
            "charger_200kW_Count, charger_300kW_Plus_Count, charge_speed, price_per_kwh, file_path) " +
            "VALUES (#{stationId}, #{name}, #{address}, #{totalCharger}, #{availableCharger}, " +
            "#{charger_7kW_Count}, #{charger_50kW_Count}, #{charger_100kW_Count}, " +
            "#{charger_200kW_Count}, #{charger_300kW_Plus_Count}, #{chargeSpeed}, #{pricePerKWh}, #{filePath})")
    void insertChargingStation(ChargingStationDTO station);

}
