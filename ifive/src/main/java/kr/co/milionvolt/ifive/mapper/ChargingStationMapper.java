package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.ChargerDTO;
import kr.co.milionvolt.ifive.domain.ChargingStationDTO;
import kr.co.milionvolt.ifive.domain.ChargingStationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ChargingStationMapper {

    // 전체 충전소 조회(충전기 타입과 충전기 상태 포함)
    @Select("SELECT cs.station_id, cs.name, cs.address, cs.total_charger, cs.available_charger, " +
            "cs.charger_7kW_count, cs.charger_50kW_count, cs.charger_100kW_count," +
            "cs.charger_200kW_count, cs.charger_300kW_plus_count, " +
            "cs.charge_speed, cs.price_per_kwh," +
            "cs.created_at, cs.file_path, c.chager_type, c.charger_status " +
            "FROM charging_station cs " +
            "JOIN charger c ON cs.station_id = c.charger_id")
    List<ChargingStationVO> getAllChargingStationList();

    // 충전소 개별 조회
    @Select("SELECT cs.station_id, cs.name, cs.address, cs.total_charger, cs.available_charger, cs.charger_7kW_count, " +
            "cs.charger_50kW_count, cs.charger_100kW_count, " +
            "cs.charger_200kW_count, cs.charger_300kW_plus_count,  cs.charge_speed, cs.price_per_kwh, " +
            "cs.created_at, cs.file_path, c.chager_type, c.charger_status " +
            "FROM charging_station cs " +
            "JOIN charger c ON cs.station_id = c.station_id " +
            "WHERE cs.station_id = #{stationId}")
    List<ChargingStationVO> getOneChargingStationList(@Param("stationId") Integer stationId);

    // 충전소 수정
    @Update("UPDATE charging_station SET name = #{name}, address = #{address}, total_charger = #{totalCharger}, " +
            "available_charger = #{availableCharger}, charger_7kW_count = #{charger_7kW_count}, charger_50kW_count = #{charger_50kW_count}, " +
            "charger_100kW_count = #{charger_100kW_count}, charger_200kW_count = #{charger_200kW_count}, charger_300kW_plus_count = #{charger_300kW_plus_count}," +
            "charge_speed = #{chargeSpeed}, price_per_kwh = #{pricePerKWh}, file_path = #{filePath} " +
            "WHERE station_id = #{stationId}")
    int updateChargingStation(ChargingStationDTO chargingStationDTO);

    // 충전기 상태 및 타입 수정 (필요에 따라)
    @Update("UPDATE charger SET chager_type = #{chagerType}, charger_status = #{chargerStatus} " +
            "WHERE charger_id = #{chargerId}")
    int updateChargerInfo(@Param("chargerId") Integer chargerId,
                          @Param("chagerType") ChargerDTO.ChagerType chagerType,
                          @Param("chargerStatus") ChargerDTO.ChargerStatus chargerStatus);

}
