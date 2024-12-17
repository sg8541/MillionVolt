package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.payment.ChargeStationInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ChargeStationInfoMapper {
    @Select("SELECT name, address, file_path FROM charging_station WHERE station_id = #{stationId}")
    ChargeStationInfoDTO selectChargeStationInfoById(int stationId);
}
