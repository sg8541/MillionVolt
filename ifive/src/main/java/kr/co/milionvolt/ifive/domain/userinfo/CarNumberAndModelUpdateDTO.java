package kr.co.milionvolt.ifive.domain.userinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarNumberAndModelUpdateDTO {
    private Integer carId;
    private String carNumber;
    private String modelId;
}
