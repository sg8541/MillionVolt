package kr.co.milionvolt.ifive.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamportConfig {

    @Bean
    public IamportClient iamportClient() {
        String apiKey = "1506657757712833"; // 포트원에서 발급받은 API 키
        String apiSecret = "gxdcrP0Ze3bQbG9bPjVenWsjHnQzADqE3nKf2YWbQcifHUMC2WeMT2Pif10HCXKf1uHZ4uHICLhu6sbb"; // 포트원에서 발급받은 API Secret
        return new IamportClient(apiKey, apiSecret);
    }
}
