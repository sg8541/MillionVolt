package kr.co.milionvolt.ifive.domain;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CarModelDTO {

  private Integer modelId;
  private String modelName;
  private Integer modelBattery;
  private String modelBrand;
  private String modelFilepath;

}
