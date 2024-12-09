package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.reservation.ReservationDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

//@Mapper
//public interface ReservationMapper {
//
//    @Insert("INSERT INTO reservation " +
//            "(reservation_id, start_time, end_time, status, created_at, " +
//            "charger_id, user_id, station_id) " + // 오타 수정
//            "VALUES (#{reservationId}, #{startTime}, #{endTime}, #{status}, " +
//            "#{createdAt}, #{chargerId}, #{userId}, #{stationId})")
//    int insertReservation(ReservationDTO reservation); // 괄호 정리
//}
@Mapper
public interface ReservationMapper {

    @Insert("""
        INSERT INTO reservation 
        (reservation_id, start_time, end_time, status, created_at, charger_id, user_id, station_id)
        VALUES 
        (#{reservationId}, #{startTime}, #{endTime}, #{status}, #{createdAt}, #{chargerId}, #{userId}, #{stationId})
        """)
    int insertReservation(ReservationDTO reservationDTO);

    @Select("SELECT count(*) " +
            "FROM reservation " +
            "WHERE start_time BETWEEN #{startTime} AND #{endTime}" +
            "OR end_time BETWEEN #{startTime} AND #{endTime}" +
            "OR (start_time <= #{startTime} AND end_Time >= #{endTime})" +
            "AND charger_id = #{chargerId}" +
            "AND status != 'cancelled'")
    int checkConflictReservation(ReservationDTO reservationDTO);
}
