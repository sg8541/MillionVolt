package kr.co.milionvolt.ifive.service.payment;
import com.siot.IamportRestClient.exception.IamportResponseException;
import kr.co.milionvolt.ifive.domain.payment.PayPriceDTO;

import java.io.IOException;

public interface PayPriceService {
    boolean savePayPrice(String impuid, PayPriceDTO payPriceDTO) throws IamportResponseException, IOException;
}
