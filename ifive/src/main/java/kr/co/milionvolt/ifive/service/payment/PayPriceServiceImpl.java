//package kr.co.milionvolt.ifive.service.payment;
//
//import com.siot.IamportRestClient.IamportClient;
//import com.siot.IamportRestClient.exception.IamportResponseException;
//import com.siot.IamportRestClient.response.IamportResponse;
//import com.siot.IamportRestClient.response.Payment;
//import kr.co.milionvolt.ifive.domain.payment.PayPriceDTO;
//import kr.co.milionvolt.ifive.mapper.PayPriceMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
//@Service
//public class PayPriceServiceImpl implements PayPriceService {
//
//    @Autowired
//    private PayPriceMapper payPriceMapper;
//
//    @Autowired
//    private IamportClient iamportClient;
//
//    public boolean verifyPayment(String imp_uid) throws IamportResponseException, IOException {
//        IamportResponse<Payment> response = iamportClient.paymentByImpUid(imp_uid);
//        Payment payment = response.getResponse();
//
//        if(payment != null && "paid".equals(payment.getStatus())) {
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public int savePayPrice(PayPriceDTO payPriceDTO) {
//        return payPriceMapper.insertPayPrice(payPriceDTO);
//    }
//}
//
//

package kr.co.milionvolt.ifive.service.payment;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import kr.co.milionvolt.ifive.domain.payment.PayPriceDTO;
import kr.co.milionvolt.ifive.mapper.ChargingStatusMapper;
import kr.co.milionvolt.ifive.mapper.PayPriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class PayPriceServiceImpl implements PayPriceService {

    @Autowired
    IamportClient iamportClient;

    @Autowired
    PayPriceMapper payPriceMapper;

    @Autowired
    ChargingStatusMapper chargingStatusMapper;

    @Override
    @Transactional // 트랜잭션 적용
    public boolean savePayPrice(String impUid, PayPriceDTO payPriceDTO) throws IamportResponseException, IOException {
        IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(impUid);
        Payment payment = paymentResponse.getResponse();

        if (payment != null && "paid".equals(payment.getStatus())) {
            try {
                // 결제 정보를 DTO에 매핑
                payPriceDTO.setImpUid(payment.getImpUid());

                // 결제 정보 DB 저장
                boolean insertResult = payPriceMapper.insertPayPrice(payPriceDTO);

                if (insertResult) {
                    // 상태 업데이트
                    chargingStatusMapper.chargingStatusAvailable(payPriceDTO.getChargeId(), payPriceDTO.getStationId());
                    return true;
                }
                return false;
            } catch (Exception e) {
                throw new RuntimeException("DB 저장 중 오류 발생", e);
            }
        }
        return false;
    }
}



