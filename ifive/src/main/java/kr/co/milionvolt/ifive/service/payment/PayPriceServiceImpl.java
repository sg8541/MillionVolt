package kr.co.milionvolt.ifive.service.payment;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import kr.co.milionvolt.ifive.domain.payment.PayPriceDTO;
import kr.co.milionvolt.ifive.mapper.PayPriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PayPriceServiceImpl implements PayPriceService {

    @Autowired
    private PayPriceMapper payPriceMapper;

    @Autowired
    private IamportClient iamportClient;

    public boolean verifyPayment(String imp_uid) throws IamportResponseException, IOException {
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(imp_uid);
        Payment payment = response.getResponse();

        if(payment != null && "paid".equals(payment.getStatus())) {
            return true;
        }
        return false;
    }

    @Override
    public int savePayPrice(PayPriceDTO payPriceDTO) {
        return payPriceMapper.insertPayPrice(payPriceDTO);
    }
}
