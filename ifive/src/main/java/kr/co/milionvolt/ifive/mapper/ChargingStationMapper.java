package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationVO;
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

    // 충전소 필터링 (속도, 상태)
    @Select("<script>" +
            "SELECT cs.station_id, cs.name, cs.address, cs.total_charger, cs.available_charger, " +
            "cs.charger_7kW_count, cs.charger_50kW_count, cs.charger_100kW_count," +
            "cs.charger_200kW_count, cs.charger_300kW_plus_count, " +
            "cs.charge_speed, cs.price_per_kwh, cs.created_at, cs.file_path, c.chager_type, c.charger_status " +
            "FROM charging_station cs " +
            "JOIN charger c ON cs.station_id = c.station_id " +
            "<where>" +
            "<if test='chargeSpeed != null'> AND cs.charge_speed = #{chargeSpeed} </if>" +
            "<if test='chargerStatus != null'> AND c.charger_status = #{chargerStatus} </if>" +
            "</where>" +
            "</script>")
    List<ChargingStationVO> getChargingStationsByFilter(@Param("chargeSpeed") ChargingStationDTO.ChargeSpeed chargeSpeed,
                                                        @Param("chargerStatus") ChargerDTO.ChargerStatus chargerStatus);


    // 사용자 위치를 기준으로 충전소 목록 조회 (위도, 경도 기준)
    @Select("<script>" +
            "SELECT cs.station_id, cs.name, cs.address, cs.total_charger, cs.available_charger, " +
            "cs.charger_7kW_count, cs.charger_50kW_count, cs.charger_100kW_count," +
            "cs.charger_200kW_count, cs.charger_300kW_plus_count, " +
            "cs.charge_speed, cs.price_per_kwh, cs.created_at, cs.file_path, c.chager_type, c.charger_status " +
            "FROM charging_station cs " +
            "JOIN charger c ON cs.station_id = c.station_id " +
            "WHERE 1=1 " +
            "<if test='chargeSpeed != null'> AND cs.charge_speed = #{chargeSpeed} </if>" +
            "<if test='chargerStatus != null'> AND c.charger_status = #{chargerStatus} </if>" +
            "<if test='latitude != null and longitude != null'>" +
            " AND (ST_Distance_Sphere(point(cs.longitude, cs.latitude), point(#{longitude}, #{latitude})) <= 10000)" +  // 10km 내의 충전소 조회
            "</if>" +
            "</script>")
    List<ChargingStationVO> getChargingStationsByLocation(@Param("latitude") double latitude,
                                                          @Param("longitude") double longitude,
                                                          @Param("chargeSpeed") String chargeSpeed,
                                                          @Param("chargerStatus") String chargerStatus);

    // 검색
    @Select("SELECT * FROM charging_station WHERE name LIKE CONCAT('%', #{query}, '%')")
    List<ChargingStationVO> searchChargingStations(@Param("query") String query);

}
