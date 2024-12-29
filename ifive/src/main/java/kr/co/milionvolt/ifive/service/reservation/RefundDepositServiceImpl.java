package kr.co.milionvolt.ifive.service.reservation;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import kr.co.milionvolt.ifive.mapper.RefundDepositMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RefundDepositServiceImpl implements RefundDepositService {

    @Autowired
    private RefundDepositMapper refundDepositMapper;

    @Autowired
    private IamportClient iamportClient;

    @Override
    public int progressRefundDeposit(int reservationId) throws IamportResponseException, IOException {

        int penaltyAmount = refundDepositMapper.selectPenaltyAmount(reservationId);

        if(penaltyAmount > 0){
            return 1;
        } else {
            String impUid = refundDepositMapper.selectImpUid(reservationId);
            CancelData cancelData = new CancelData(impUid, true);
            iamportClient.cancelPaymentByImpUid(cancelData);
            refundDepositMapper.updateRefundStatus(reservationId);
            return 0;
        }
    }
}
