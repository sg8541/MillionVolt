package kr.co.milionvolt.ifive.mapper;

import kr.co.milionvolt.ifive.domain.penaltie.PenaltieDTO;
import kr.co.milionvolt.ifive.domain.penaltie.PenaltiechargerStatusCheckVO;
import kr.co.milionvolt.ifive.domain.penaltie.PenaltyCheckVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PenaltyMapper {

    @Select(" SELECT c.charger_id ,c.charger_status_id" +
            " FROM charger c " +
            " JOIN reservation r " +
            " ON c.charger_id = r.charger_id " +
            " WHERE r.reservation_id=#{reservationId}")
    public PenaltiechargerStatusCheckVO findChargerId(int reservationId); // 예약의 대한 충전기 조회

    @Select(" SELECT r.start_time " +
            " FROM charger c" +
            " JOIN reservation r" +
            " ON c.charger_id=r.charger_id " +
            " WHERE c.charger_id = #{chargerId} " +
            " AND r.start_time > #{entTime}" +
            " ORDER BY r.start_time" +
            " LIMIT 1")
    public LocalDateTime findCloseStartTime(LocalDateTime entTime, int chargerId); // 충전기의 대한 가까운 예약 조회.

    @Insert(" INSERT INTO penalty(penalty_amount, reason, created_at,reservation_id) " +
            " VALUES (#{penaltyAmount}, #{reason}, #{createdAt}, #{reservationId}) ")
    public void insertPenalty(PenaltieDTO penaltieDTO);

    @Update(" UPDATE penalty SET penalty_amount = #{penaltyAmount}" +
            " WHERE reservation_id = #{reservationId}")
    public void updatePenalty(int penaltyAmount, int reservationId);

    @Select(" SELECT penalty_id, penalty_amount FROM penalty WHERE reservation_id = #{reservationId} ")
    public PenaltyCheckVO penaltyCheckVo(int reservationId);
}
