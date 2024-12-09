package kr.co.milionvolt.ifive.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamportConfig {

    @Bean
    public IamportClient iamportClient() {
        String apiKey = ""; // 포트원에서 발급받은 API 키
        String apiSecret = ""; // 포트원에서 발급받은 API Secret
        return new IamportClient(apiKey, apiSecret);
    }
}
