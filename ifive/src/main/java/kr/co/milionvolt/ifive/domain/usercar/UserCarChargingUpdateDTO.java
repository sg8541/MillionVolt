package kr.co.milionvolt.ifive.domain.usercar;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserCarChargingUpdateDTO {
    private Integer carId;
    private double carBattery;
}
