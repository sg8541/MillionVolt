//package kr.co.milionvolt.ifive.csv;
//
//import kr.co.milionvolt.ifive.domain.charger.ChargerDTO;
//import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class CsvToDtoConverter {
//
//    private final Map<String, Integer> chargeSpeedMap;
//
//    public CsvToDtoConverter() {
//        this.chargeSpeedMap = new HashMap<>();
//        chargeSpeedMap.put("7kW", 1);
//        chargeSpeedMap.put("50kW", 2);
//        chargeSpeedMap.put("100kW", 3);
//        chargeSpeedMap.put("200kW", 4);
//        chargeSpeedMap.put("300kW", 5);
//    }
//
//    public Map<String, Object> convertCsvToDto(String resourcePath) {
//        List<ChargingStationDTO> stationList = new ArrayList<>();
//        List<ChargerDTO> chargerList = new ArrayList<>();
//
//        try (BufferedReader br = new BufferedReader(new InputStreamReader(
//                getClass().getClassLoader().getResourceAsStream(resourcePath), "UTF-8"))) {
//
//            String line;
//            br.readLine(); // 헤더 건너뛰기
//
//            Map<String, ChargingStationDTO> stationMap = new HashMap<>();
//            int stationIdCounter = 1;
//            int chargerIdCounter = 1;
//
//            while ((line = br.readLine()) != null) {
//                String[] values = line.split(",");
//
//                // 충전소 처리
//                String stationKey = values[1]; // 충전소명
//                ChargingStationDTO station = stationMap.getOrDefault(stationKey, new ChargingStationDTO());
//                if (!stationMap.containsKey(stationKey)) {
//                    station.setStationId(stationIdCounter++);
//                    station.setName(values[1]);
//                    station.setAddress(values[0]);
//                    station.setFacilityType(values[2]);
//                    station.setTotalCharger(0);
//
//                    // charge_speed 처리
//                    String rawChargeSpeed = values.length > 4 ? values[4] : null;
//                    String extractedSpeed = extractChargeSpeed(rawChargeSpeed); // "100kW"
//                    Integer chargerSpeedId = chargeSpeedMap.getOrDefault(extractedSpeed, null);
//                    station.setChargerSpeedId(chargerSpeedId);
//
//                    station.setDeviceType(values.length > 3 && !values[3].isEmpty() ? values[3] : "Unknown");
//                    station.setChargerType(values.length > 5 && !values[5].isEmpty() ? values[5] : "Unknown");
//                    station.setChargerStatusId(null);
//
//                    stationMap.put(stationKey, station);
//                    stationList.add(station);
//
//                    // 디버깅 로그
//                    System.out.println("Station: " + station);
//                }
//
//                // 충전기 처리
//                ChargerDTO charger = new ChargerDTO();
//                charger.setChargerId(chargerIdCounter++);
//                charger.setStationId(station.getStationId());
//                charger.setChargerSpeedId(station.getChargerSpeedId());
//                charger.setChargerStatusId(null);
//                chargerList.add(charger);
//
//                // 충전소의 총 충전기 수 업데이트
//                station.setTotalCharger(station.getTotalCharger() + 1);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("stations", stationList);
//        result.put("chargers", chargerList);
//
//        return result;
//    }
//
//    private String extractChargeSpeed(String rawSpeed) {
//        if (rawSpeed == null || rawSpeed.isEmpty()) {
//            return null;
//        }
//        Pattern pattern = Pattern.compile("\\d+kW");
//        Matcher matcher = pattern.matcher(rawSpeed);
//        return matcher.find() ? matcher.group() : null;
//    }
//}
