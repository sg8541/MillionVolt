package kr.co.milionvolt.ifive.service.payment;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import kr.co.milionvolt.ifive.mapper.CancelPaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CancelPaymentServiceImpl implements CancelPaymentService {

    @Autowired
    CancelPaymentMapper cancelPaymentMapper;

    @Autowired
    IamportClient iamportClient;

    @Override
    public String cancelPayment(int userId, int paymentId) throws IamportResponseException, IOException {

        // DB에서 imp_uid 조회
        String impUid = cancelPaymentMapper.selectImpUid(userId, paymentId);

        if (impUid == null) {
            // imp_uid가 없으면 오류 반환
            return "해당 결제 정보가 존재하지 않습니다.";
        }

        // imp_uid로 결제 정보 조회
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
        Payment payment = response.getResponse();

        // 결제가 "paid" 상태인 경우에만 취소 가능
        if (payment != null && "paid".equals(payment.getStatus())) {
            System.out.println(payment.getStatus());
            CancelData cancelData = new CancelData(impUid, true);

            // 결제 취소 요청
            IamportResponse<Payment> cancelResponse = iamportClient.cancelPaymentByImpUid(cancelData);

            //결제 취소 반응 확인
            System.out.println(cancelResponse.getResponse());

            // 취소가 성공한 경우
            if (cancelResponse.getResponse() != null) {

                // 결제 취소 후 상태 업데이트
                boolean updateResult = cancelPaymentMapper.updateStatus(impUid);

                // 상태 업데이트 성공 시
                if (updateResult) {
                    return "결제 취소가 완료되었습니다.";
                } else {
                    return "결제 상태 업데이트에 실패했습니다.";
                }
            } else {
                return "결제 취소 요청에 실패했습니다: " + cancelResponse.getMessage()  + "  " + cancelResponse.getCode();
            }
        } else {
            return "결제 상태가 'paid'가 아니거나 결제를 찾을 수 없습니다.";
        }
    }
}
//package kr.co.milionvolt.ifive.service.payment;
//
//import com.siot.IamportRestClient.IamportClient;
//import com.siot.IamportRestClient.exception.IamportResponseException;
//import com.siot.IamportRestClient.request.CancelData;
//import com.siot.IamportRestClient.response.IamportResponse;
//import com.siot.IamportRestClient.response.Payment;
//import kr.co.milionvolt.ifive.mapper.CancelPaymentMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
//@Service
//public class CancelPaymentServiceImpl implements CancelPaymentService {
//
//    private final CancelPaymentMapper cancelPaymentMapper;
//    private final IamportClient iamportClient;
//
//    @Autowired
//    public CancelPaymentServiceImpl(CancelPaymentMapper cancelPaymentMapper) {
//        this.cancelPaymentMapper = cancelPaymentMapper;
//        this.iamportClient = new IamportClient("YOUR_API_KEY", "YOUR_API_SECRET");
//    }
//
//    @Override
//    public String cancelPayment(int userId, int paymentId) throws IamportResponseException, IOException {
//
//        // DB에서 imp_uid 조회
//        String impUid = cancelPaymentMapper.selectImpUid(userId, paymentId);
//
//        if (impUid == null) {
//            return "해당 결제 정보가 존재하지 않습니다.";
//        }
//
//        // imp_uid로 결제 정보 조회
//        IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
//        Payment payment = response.getResponse();
//
//        if (payment != null && "paid".equals(payment.getStatus())) {
//            CancelData cancelData = new CancelData(impUid, false);
//            cancelData.setReason("고객의 결제 취소");
//
//            try {
//                IamportResponse<Payment> cancelResponse = iamportClient.cancelPaymentByImpUid(cancelData);
//
//                if (cancelResponse.getCode() == 0) {
//                    boolean updateResult = cancelPaymentMapper.updateStatus(impUid);
//                    if (updateResult) {
//                        return "결제 취소가 완료되었습니다.";
//                    } else {
//                        throw new IllegalStateException("결제 취소 상태 업데이트 실패: imp_uid=" + impUid);
//                    }
//                } else {
//                    return "결제 취소 요청에 실패했습니다: " + cancelResponse.getMessage();
//                }
//            } catch (IamportResponseException | IOException e) {
//                e.printStackTrace();
//                return "결제 취소 중 오류 발생: " + e.getMessage();
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "알 수 없는 오류가 발생했습니다: " + e.getMessage();
//            }
//        } else {
//            return "결제 상태가 'paid'가 아니거나 결제를 찾을 수 없습니다.";
//        }
//    }
//}
