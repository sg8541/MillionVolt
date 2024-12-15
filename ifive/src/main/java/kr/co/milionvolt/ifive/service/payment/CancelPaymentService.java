package kr.co.milionvolt.ifive.service.payment;

import com.siot.IamportRestClient.exception.IamportResponseException;

import java.io.IOException;

public interface CancelPaymentService {
    public String cancelPayment(int userId, int paymentId) throws IamportResponseException, IOException;
}
