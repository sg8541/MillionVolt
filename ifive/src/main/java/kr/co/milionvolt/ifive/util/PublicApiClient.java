package kr.co.milionvolt.ifive.util;

import kr.co.milionvolt.ifive.domain.chargingstation.ChargingStationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class PublicApiClient {

    private final RestTemplate restTemplate;

    @Autowired
    public PublicApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 공공 API에서 충전소 데이터 가져오기
    public List<ChargingStationDTO> fetchChargingStationData() {
        // 공공 API의 URL
        String apiUrl = "https://api.odcloud.kr/api/15125089/v1/uddi:00dd2278-6e4b-459e-9ad2-b7b3f876513a?page=1&perPage=10";

        // API 호출 및 결과 반환
        ResponseEntity<List<ChargingStationDTO>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ChargingStationDTO>>() {}
        );

        return response.getBody();
    }

}

