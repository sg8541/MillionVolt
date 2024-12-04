package kr.co.milionvolt.ifive.service.charging;

import kr.co.milionvolt.ifive.domain.notification.ChargingStatusDTO;
import org.springframework.stereotype.Service;

@Service
public interface ChargingStatusSerivce {
    public ChargingStatusDTO chargingStatus(String userId, int reservationId);
}
