package kr.co.milionvolt.ifive.service;

import kr.co.milionvolt.ifive.csv.CsvToDtoConverter;
import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
import kr.co.milionvolt.ifive.mapper.ChargerMapper;
import kr.co.milionvolt.ifive.mapper.ChargingStationMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CsvImportService {

    private final ChargingStationMapper stationMapper;
    private final ChargerMapper chargerMapper;

    public CsvImportService(ChargingStationMapper stationMapper, ChargerMapper chargerMapper) {
        this.stationMapper = stationMapper;
        this.chargerMapper = chargerMapper;
    }

    public void importCsvToDb(String resourcePath) {
        CsvToDtoConverter converter = new CsvToDtoConverter();
        Map<String, Object> data = converter.convertCsvToDto(resourcePath);

        List<ChargingStationDTO> stations = (List<ChargingStationDTO>) data.get("stations");
        List<ChargerDTO> chargers = (List<ChargerDTO>) data.get("chargers");

        // 충전소 저장
        for (ChargingStationDTO station : stations) {
            if (stationMapper.findStationByNameAndAddress(station.getName(), station.getAddress()) == null) {
                stationMapper.insertChargingStation(station);
                System.out.println("Inserted Station: " + station);
            } else {
                System.out.println("Duplicate Station Skipped: " + station);
            }
        }

        // 충전기 저장
        for (ChargerDTO charger : chargers) {
            // 중복 확인 로직
            if (chargerMapper.findChargerByStationAndSpeed(charger.getStationId(), charger.getChargerSpeedId()) == null) {
                charger.setChargerId(null); // DB가 자동 생성하도록 설정
                chargerMapper.insertCharger(charger);
                System.out.println("Inserted Charger: " + charger);
            } else {
                System.out.println("Duplicate Charger Skipped: " + charger);
            }
        }
    }
}