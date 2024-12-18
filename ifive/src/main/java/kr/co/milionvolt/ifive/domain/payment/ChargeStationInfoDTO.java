package kr.co.milionvolt.ifive.domain.payment;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChargeStationInfoDTO {
    String name;
    String address;
    String filePath;
}
