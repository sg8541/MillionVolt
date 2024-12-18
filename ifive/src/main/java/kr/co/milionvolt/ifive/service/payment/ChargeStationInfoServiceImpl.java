package kr.co.milionvolt.ifive.service.payment;

import kr.co.milionvolt.ifive.domain.payment.ChargeStationInfoDTO;
import kr.co.milionvolt.ifive.mapper.ChargeStationInfoMapper;
import kr.co.milionvolt.ifive.mapper.ChargingStationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChargeStationInfoServiceImpl implements ChargeStationInfoService {
    @Autowired
    ChargeStationInfoMapper chargeStationInfoMapper;

    @Override
    public ChargeStationInfoDTO printStatinInfo(int stationId) {
        ChargeStationInfoDTO chargeStationInfoDTO = chargeStationInfoMapper.selectChargeStationInfoById(stationId);
        return chargeStationInfoDTO;
    }
}
