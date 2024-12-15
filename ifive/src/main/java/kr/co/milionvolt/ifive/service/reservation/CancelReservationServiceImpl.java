package kr.co.milionvolt.ifive.service.reservation;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import kr.co.milionvolt.ifive.mapper.CancelReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CancelReservationServiceImpl implements CancelReservationService {

    @Autowired
    private IamportClient iamportClient;

    @Autowired
    private CancelReservationMapper cancelReservationMapper;

    @Override
    public String cancelReservation(int userId, int reservationId) throws IamportResponseException, IOException {

        String impUid = cancelReservationMapper.selectReservationImpUid(userId, reservationId);

        if(impUid == null) {
            return "예약정보가 없습니다.";
        }

        IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
        Payment payment = response.getResponse();

        if(payment != null && "paid".equals(payment.getStatus())) {
            CancelData cancelData = new CancelData(impUid, true);
            IamportResponse<Payment> cancelResponse = iamportClient.cancelPaymentByImpUid(cancelData);

            if (cancelResponse.getResponse() != null) {

                boolean updateResult = cancelReservationMapper.updateReservationStatus(impUid);

                if (updateResult) {
                    return "예약취소가 완료되었습니다.";
                } else {
                    return "예약취소에 실패했습니다.";
                }
            } else {
                return "결제 취소 요청에 실패했습니다: " + cancelResponse.getMessage() + "  " + cancelResponse.getCode();
            }
        } else {
            return "결제 상태가 'paid'가 아니거나 결제를 찾을 수 없습니다.";
        }
    }
}
