package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationVO;
import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChargingStationMapper {

    // 1. 특정 충전소 이름과 주소로 중복 확인
    @Select("SELECT * FROM charging_station WHERE name = #{name} AND address = #{address}")
    ChargingStationDTO findStationByNameAndAddress(@Param("name") String name, @Param("address") String address);

    // 2. 특정 충전 속도를 가진 충전소 조회
    @Select("""
        SELECT station_id, name, address, total_charger, available_charger, charger_speed_id,
               price_per_kwh, file_path, facility_type, device_type, charger_type, charger_status_id
        FROM charging_station
        WHERE charger_speed_id = #{chargerSpeedId}
        LIMIT #{offset}, #{size}
""")
    List<ChargingStationVO> findStationsByChargeSpeed(@Param("chargerSpeedId") Integer chargerSpeedId,
                                                      @Param("offset") int offset,
                                                      @Param("size") int size);


    // 3. 모든 충전소 페이징 조회
    @Select("""
        SELECT station_id, name, address, total_charger, available_charger, charger_speed_id,
               price_per_kwh, file_path, facility_type, device_type, charger_type, charger_status_id
        FROM charging_station
        LIMIT #{offset}, #{size}
    """)
    List<ChargingStationVO> getAllChargingStations(@Param("offset") int offset, @Param("size") int size);

    // 2. 특정 충전소 조회 (모달창 데이터 제공)
    @Select("""
            SELECT station_id, name, address, total_charger, available_charger, charger_speed_id, 
                   price_per_kwh, file_path, facility_type, device_type, charger_type, charger_status_id
            FROM charging_station
            WHERE station_id = #{stationId}
            """)
    ChargingStationVO getChargingStationById(@Param("stationId") Integer stationId);

    // 3. 충전소 등록
    @Insert("INSERT INTO charging_station (station_id, name, address, total_charger, facility_type, charger_speed_id) "
            + "VALUES (#{stationId}, #{name}, #{address}, #{totalCharger}, #{facilityType}, #{chargerSpeedId})")
    void insertChargingStation(ChargingStationDTO station);


    // 4. 충전소 검색 (주소 및 이름 검색, 페이징 포함)
    @Select("""
            SELECT station_id, name, address, total_charger, available_charger, charger_speed_id, 
                   price_per_kwh, file_path, facility_type, device_type, charger_type, charger_status_id
            FROM charging_station
            WHERE name LIKE CONCAT('%', #{query}, '%') OR address LIKE CONCAT('%', #{query}, '%')
            LIMIT #{offset}, #{size}
            """)
    List<ChargingStationVO> searchChargingStations(@Param("query") String query,
                                                   @Param("offset") int offset,
                                                   @Param("size") int size);

    // 6. 주변 충전소 상태 (사이드바 표시 데이터)
    @Select("""
            SELECT station_id, name, address, total_charger, available_charger, charger_speed_id, 
                   price_per_kwh, file_path, facility_type, device_type, charger_type, charger_status_id
            FROM charging_station
            WHERE address LIKE CONCAT('%', #{address}, '%')
            """)
    List<ChargingStationVO> getSidebarStations(@Param("address") String address);

    // 7. 특정 충전소에 속한 충전기 목록 조회
    @Select("""
            SELECT *
            FROM charger
            WHERE station_id = #{stationId} AND charger_id = #{chargerId}
""")
    List<ChargerDTO> findChargersByStationId(@Param("stationId") Integer stationId);

    @Select("""
            SELECT *
            FROM charging_station
            WHERE station_id = #{stationId}
""")
    ChargingStationDTO findStationById(@Param("stationId") Integer stationId);

    // 9. 주변 충전소 조회 (주소 키워드 기반)
    @Select("SELECT * FROM charging_station WHERE address LIKE CONCAT('%', #{address}, '%')")
    List<ChargingStationVO> findStationsByAddress(@Param("address") String address);

    // 8. 특정 충전소와 충전기 정보를 함께 조회
    @Select("""
    SELECT 
        cs.station_id AS stationId, cs.name AS stationName, cs.address AS stationAddress, cs.total_charger AS totalCharger,
        cs.available_charger AS availableCharger, cs.charger_speed_id AS chargerSpeedId, cs.price_per_kwh AS pricePerKWh,
        cs.file_path AS filePath, cs.facility_type AS facilityType, cs.device_type AS deviceType,
        cs.charger_type AS chargerType
    FROM charging_station cs
    WHERE cs.station_id = #{stationId}
""")
    @Results(id = "ChargingStationResultMap", value = {
            @Result(property = "stationId", column = "stationId"),
            @Result(property = "name", column = "stationName"),
            @Result(property = "address", column = "stationAddress"),
            @Result(property = "totalCharger", column = "totalCharger"),
            @Result(property = "availableCharger", column = "availableCharger"),
            @Result(property = "chargerSpeedId", column = "chargerSpeedId"),
            @Result(property = "pricePerKWh", column = "pricePerKWh"),
            @Result(property = "filePath", column = "filePath"),
            @Result(property = "facilityType", column = "facilityType"),
            @Result(property = "deviceType", column = "deviceType"),
            @Result(property = "chargerType", column = "chargerType"),
            @Result(property = "chargers", column = "stationId", javaType = List.class,
                    many = @Many(select = "findChargersByStationId"))
    })
    ChargingStationDTO findStationWithChargers(@Param("stationId") Integer stationId);


    // 10. 검색어와 주소 기반으로 페이징된 충전소 목록 조회
    @Select("""
    SELECT station_id, name, address, total_charger, available_charger, charger_speed_id, 
           price_per_kwh, file_path, facility_type, device_type, charger_type, charger_status_id
    FROM charging_station
    WHERE address LIKE CONCAT('%', #{address}, '%') OR name LIKE CONCAT('%', #{query}, '%')
    LIMIT #{offset}, #{size}
""")
    List<ChargingStationVO> getStationsWithPaging(@Param("address") String address,
                                                  @Param("query") String query,
                                                  @Param("offset") int offset,
                                                  @Param("size") int size);

}