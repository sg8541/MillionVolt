package kr.co.milionvolt.ifive.service.penalty;

import kr.co.milionvolt.ifive.domain.penaltie.PenaltieDTO;
import kr.co.milionvolt.ifive.domain.penaltie.PenaltiechargerStatusCheckVO;
import kr.co.milionvolt.ifive.domain.penaltie.PenaltyCheckVO;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface PenaltyService {
    public PenaltiechargerStatusCheckVO findChargerId(int reservationId ,int stationId , int chargerId);
    public LocalDateTime findCloseStratTime(LocalDateTime entTime, int chargerId, int stationId);
    public void insertPenalty(PenaltieDTO dto);
    public void updatePenalty(int Amount, int reservationId);
    public PenaltyCheckVO penaltyCheckVo(int reservationId);
    public int selectPenaltyAmount(int reservationId);
    public void insertPenaltyRefund(Timestamp createdAt, int reservationId, String refundStatus);
}
