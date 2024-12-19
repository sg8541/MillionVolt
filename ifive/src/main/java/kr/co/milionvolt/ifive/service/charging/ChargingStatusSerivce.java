package kr.co.milionvolt.ifive.service.charging;

import kr.co.milionvolt.ifive.domain.notification.ChargingStatusDTO;
import kr.co.milionvolt.ifive.domain.usercar.UserCarChargingUpdateDTO;
import org.springframework.stereotype.Service;

@Service
public interface ChargingStatusSerivce {
    public ChargingStatusDTO chargingStatus(String userId, int reservationId,int stationId);
    public void chargingUpdate(int carId, double carBattery);
    public void chargingStatusInuse(int chargerId, int stationId);
}
