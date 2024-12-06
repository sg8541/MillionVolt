package kr.co.milionvolt.ifive.domain.chargingstation;

import lombok.Data;

import java.util.List;

@Data
public class ChargingStationApiResponse {
    private List<ChargingStationVO> data;
}
