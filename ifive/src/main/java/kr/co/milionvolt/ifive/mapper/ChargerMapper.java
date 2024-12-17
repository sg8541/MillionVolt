package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.charger.ChargerVO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChargerMapper {

    // 중복 확인
    @Select("""
    SELECT c.*, cs.charger_status AS chargerStatusName
    FROM charger c
    LEFT JOIN charger_status cs
    ON c.charger_status_id = cs.charger_status_id
    WHERE c.station_id = #{stationId} 
    AND c.charger_id = #{chargerId} 
    AND c.charger_speed_id = #{chargerSpeedId}
""")
    @Results({
            @Result(property = "chargerId", column = "charger_id"),
            @Result(property = "stationId", column = "station_id"),
            @Result(property = "chargerSpeedId", column = "charger_speed_id"),
            @Result(property = "chargerStatusId", column = "charger_status_id"),
            @Result(property = "chargerStatusName", column = "chargerStatusName") // 상태명 매핑
    })
    ChargerDTO findChargerByStationAndSpeed(@Param("stationId") Integer stationId,
                                            @Param("chargerId") Integer chargerId,
                                            @Param("chargerSpeedId") Integer chargerSpeedId);


    @Select("""
    SELECT 
        c.charger_id AS chargerId, 
        c.station_id AS stationId, 
        c.charger_status_id AS chargerStatusId,
        c.charger_speed_id AS chargerSpeedId, 
        cs.charger_type AS type
    FROM charger c
    JOIN charging_station cs ON c.station_id = cs.station_id
    WHERE c.station_id = #{stationId}
""")
    List<Map<String, Object>> findChargersWithDetails(@Param("stationId") Integer stationId);


    // 1. 특정 충전소의 모든 충전기 조회
    @Select("""
    SELECT c.charger_id, c.station_id, c.charger_speed_id, c.charger_status_id, cs.charger_status AS statusName
    FROM charger c
    LEFT JOIN charger_status cs ON c.charger_status_id = cs.charger_status_id
    WHERE c.station_id = #{stationId}
""")
    List<ChargerDTO> getChargersByStationId(@Param("stationId") Integer stationId);

    // 2. 특정 충전기의 상태 조회
    @Select("""
    SELECT charger_id, station_id, charger_status_id, charger_speed_id
    FROM charger
    WHERE station_id = #{stationId} AND charger_id = #{chargerId}
""")
    ChargerVO getChargerById(@Param("stationId") Integer stationId, @Param("chargerId") Integer chargerId);

    // 3. 충전기 상태 변경
    @Update("""
    UPDATE charger
    SET charger_status_id = #{status}
    WHERE station_id = #{stationId} AND charger_id = #{chargerId}
""")
    int updateChargerStatus(@Param("stationId") Integer stationId, @Param("chargerId") Integer chargerId, @Param("status") Integer status);

    // 4. 특정 충전소의 예약 가능한 충전기 조회
    @Select("""
    SELECT charger_id, station_id, charger_status_id, charger_speed_id
    FROM charger
    WHERE station_id = #{stationId} AND charger_status_id = 1
""")
    List<ChargerVO> getAvailableChargersByStationId(@Param("stationId") Integer stationId);

    // 5. 충전기 상태에 따른 필터링
    @Select("""
            SELECT charger_id, station_id, charger_status_id, charger_speed_id
            FROM charger
            WHERE charger_status_id = #{status}
            """)
    List<ChargerVO> getChargersByStatus(@Param("status") Integer status);

    @Insert("""
    INSERT INTO charger (charger_id, station_id, charger_status_id, charger_speed_id)
    VALUES (#{chargerId}, #{stationId}, #{chargerStatusId}, #{chargerSpeedId})
""")
    void insertCharger(ChargerDTO charger);

}