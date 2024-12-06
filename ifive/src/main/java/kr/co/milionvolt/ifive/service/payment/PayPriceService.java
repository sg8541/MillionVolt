package kr.co.milionvolt.ifive.service.payment;

import kr.co.milionvolt.ifive.domain.payment.PayPriceDTO;

public interface PayPriceService {
    int savePayPrice(PayPriceDTO payPriceDTO);
}
