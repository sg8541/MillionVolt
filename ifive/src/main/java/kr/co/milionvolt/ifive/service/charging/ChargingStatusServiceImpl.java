package kr.co.milionvolt.ifive.service.charging;

import kr.co.milionvolt.ifive.domain.notification.ChargingStatusDTO;
import kr.co.milionvolt.ifive.mapper.ChargingStationMapper;
import kr.co.milionvolt.ifive.mapper.ChargingStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChargingStatusServiceImpl implements ChargingStatusSerivce{

    @Autowired
    private ChargingStatusMapper chargingStatusMapper;

    @Override
    public ChargingStatusDTO chargingStatus(String userId, int reservationId) {
        ChargingStatusDTO dto =  chargingStatusMapper.chargingStatus(userId,reservationId);
        return dto;
    }
}
