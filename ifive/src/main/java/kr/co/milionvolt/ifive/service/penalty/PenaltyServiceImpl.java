package kr.co.milionvolt.ifive.service.penalty;

import kr.co.milionvolt.ifive.domain.penaltie.PenaltieDTO;
import kr.co.milionvolt.ifive.domain.penaltie.PenaltiechargerStatusCheckVO;
import kr.co.milionvolt.ifive.domain.penaltie.PenaltyCheckVO;
import kr.co.milionvolt.ifive.mapper.PenaltyMapper;
import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class PenaltyServiceImpl implements PenaltyService{
    @Autowired
    private PenaltyMapper penaltyMapper;

    @Override
    public PenaltiechargerStatusCheckVO findChargerId(int reservationId,int stationId, int chargerId) {
        PenaltiechargerStatusCheckVO penaltiechargerStatusCheckVO = null;
        try{
            penaltiechargerStatusCheckVO= penaltyMapper.findChargerId(reservationId, stationId, chargerId);
            if (penaltiechargerStatusCheckVO == null) {
                System.out.println("예약 ID에 해당하는 충전기를 찾을 수 없습니다.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return penaltiechargerStatusCheckVO;
    }


    @Override
    public LocalDateTime findCloseStratTime(LocalDateTime entTime,int chargerId, int stationId) {
        LocalDateTime localDateTime = null;
        try{
            System.out.println(chargerId);
            localDateTime =  penaltyMapper.findCloseStartTime(entTime, chargerId, stationId);
            System.out.println(localDateTime);
            if(localDateTime == null){
                System.out.println("예약 내역이 존재하지 않음. ");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return localDateTime;
    }

    @Override
    public void insertPenalty(PenaltieDTO dto) {
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        try{
            dto.setCreatedAt(timestamp);
            String reason = "출차안함.";
            dto.setReason(reason);
            penaltyMapper.insertPenalty(dto);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updatePenalty(int amount, int reservationId) {
        try{
            penaltyMapper.updatePenalty(amount, reservationId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public PenaltyCheckVO penaltyCheckVo(int reservationId) {
        PenaltyCheckVO penaltyCheckVO = null;
        try{
            penaltyCheckVO =  penaltyMapper.penaltyCheckVo(reservationId);
        }catch (Exception e){
            e.printStackTrace();
        }

        return penaltyCheckVO;
    }

    @Override
    public int selectPenaltyAmount(int reservationId) {
        int num =0;
        try{
            int result = penaltyMapper.selectPenatlyAmount(reservationId);
            if(result != 0){
                num =result;
            }else{
                num = 0;
            }
        }catch (BindingException e){
            System.out.println("결과가 없으면 0 리턴");
        }
        return num;
    }
}
