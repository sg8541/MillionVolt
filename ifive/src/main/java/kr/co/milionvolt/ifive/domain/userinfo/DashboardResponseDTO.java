package kr.co.milionvolt.ifive.domain.userinfo;

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
    private List<UserDashboradChartDTO> paymentChartList;
}
