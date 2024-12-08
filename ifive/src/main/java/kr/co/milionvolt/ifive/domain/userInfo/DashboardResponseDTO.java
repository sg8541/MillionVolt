package kr.co.milionvolt.ifive.domain.userInfo;

import kr.co.milionvolt.ifive.domain.payment.UserInfoPaymentListVO;
import kr.co.milionvolt.ifive.domain.reservation.UserInfoReservationListVO;
import kr.co.milionvolt.ifive.domain.user.UserDashboradUserCarDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponseDTO {
    private UserDashboradUserCarDTO userCarInfo;
    private List<UserInfoReservationListVO> reservationList;
    private List<UserInfoPaymentListVO> paymentList;
}
