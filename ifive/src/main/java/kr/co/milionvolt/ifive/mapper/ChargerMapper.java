package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.charger.ChargerVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChargerMapper {

    // 중복 확인
    @Select("SELECT * FROM charger WHERE station_id = #{stationId} AND charger_speed_id = #{chargerSpeedId}")
    ChargerDTO findChargerByStationAndSpeed(@Param("stationId") Integer stationId, @Param("chargerSpeedId") Integer chargerSpeedId);

    // 1. 특정 충전소의 모든 충전기 조회
    @Select("SELECT charger_id, charger_speed_id, charger_status_id FROM charger WHERE station_id = #{stationId}")
    List<ChargerDTO> getChargersByStationId(@Param("stationId") Integer stationId);

    // 2. 특정 충전기의 상태 조회
    @Select("""
            SELECT charger_id, station_id, charger_status_id, charger_speed_id
            FROM charger
            WHERE charger_id = #{chargerId}
            """)
    ChargerVO getChargerById(@Param("chargerId") Integer chargerId);

    // 3. 충전기 상태 변경
    @Update("""
            UPDATE charger
            SET charger_status_id = #{status}
            WHERE charger_id = #{chargerId}
            """)
    int updateChargerStatus(@Param("chargerId") Integer chargerId, @Param("status") String status);

    // 4. 특정 충전소의 예약 가능한 충전기 조회
    @Select("""
            SELECT charger_id, station_id, charger_status_id, charger_speed_id
            FROM charger
            WHERE station_id = #{stationId} AND charger_status_id = 'AVAILABLE'
            """)
    List<ChargerVO> getAvailableChargersByStationId(@Param("stationId") Integer stationId);

    // 5. 충전기 상태에 따른 필터링
    @Select("""
            SELECT charger_id, station_id, charger_status_id, charger_speed_id
            FROM charger
            WHERE charger_status_id = #{status}
            """)
    List<ChargerVO> getChargersByStatus(@Param("status") String status);

    @Insert("""
        INSERT INTO charger (charger_id, station_id, charger_status_id, charger_speed_id)
        VALUES (#{chargerId}, #{stationId}, #{chargerStatusId}, #{chargerSpeedId})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "chargerId")
    void insertCharger(ChargerDTO charger);


}
