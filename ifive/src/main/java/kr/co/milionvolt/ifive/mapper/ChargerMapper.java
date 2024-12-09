package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.charger.ChargerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface ChargerMapper {

    // 충전기 전체 조회
    @Select("SELECT * FROM charger")
    List<ChargerVO> getChargers();

    // 특정 충전소의 전체 충전기 목록 조회
    @Select("SELECT c.charger_id, c.station_id, c.charger_type, c.charger_status, c.created_at " +
            "FROM charger c " +
            "JOIN charging_station cs ON c.station_id = cs.station_id " +
            "WHERE cs.station_id = #{stationId}")
    List<ChargerVO> getChargersByStationId(@Param("stationId") Integer stationId);

    // 특정 충전소의 특정 충전기의 상태 조회
    @Select("SELECT c.charger_status " +
            "FROM charger c " +
            "JOIN charging_station cs ON c.station_id = cs.station_id " +
            "WHERE cs.station_id = #{stationId} " +
            "AND c.charger_id = #{chargerId}")
    ChargerDTO.ChargerStatus getChargerStatusByStationIdAndChargerId(@Param("stationId") Integer stationId,
                                                                     @Param("chargerId") Integer chargerId);

    // 충전기 상태 변경
    @Update("UPDATE charger SET charger_status = #{status} WHERE charger_id = #{chargerId}")
    int updateChargerStatus(@Param("chargerId") Integer chargerId, @Param("status") String status);

    // 충전기 상태 필터링
    @Select("SELECT * FROM charger WHERE charger_status = #{status}")
    List<ChargerVO> getChargersByStatus(@Param("status") ChargerDTO.ChargerStatus status);
}
