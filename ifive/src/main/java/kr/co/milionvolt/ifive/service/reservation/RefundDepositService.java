package kr.co.milionvolt.ifive.service.reservation;

import com.siot.IamportRestClient.exception.IamportResponseException;

import java.io.IOException;

public interface RefundDepositService {
    int progressRefundDeposit(int reservationId) throws IamportResponseException, IOException;
}
