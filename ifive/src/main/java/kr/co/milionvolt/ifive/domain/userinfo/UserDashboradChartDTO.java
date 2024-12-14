package kr.co.milionvolt.ifive.domain.userinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDashboradChartDTO {
    private String amount;
    private String updatedAt;
}
