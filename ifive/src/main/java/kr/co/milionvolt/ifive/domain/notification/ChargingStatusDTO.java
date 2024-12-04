package kr.co.milionvolt.ifive.domain.notification;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChargingStatusDTO {
    private String email;
    private String username;
    private double carBattery;
    private double modelBattery;
    private Timestamp startTime;
    private Timestamp enTimestamp;
    private Integer stationId;
    private String name;
    private String address;
    private double pricePerKWh;
    private String chargerType;
}
