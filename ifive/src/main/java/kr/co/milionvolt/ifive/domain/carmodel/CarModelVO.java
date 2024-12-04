package kr.co.milionvolt.ifive.domain.carmodel;

import lombok.Getter;

@Getter
public class CarModelVO {
    private final Integer modelId;
    private final String modelName;
    private final Integer modelBattery;
    private final String modelBrand;

    public CarModelVO(Integer modelId, String modelName, Integer modelBattery, String modelBrand) {
        this.modelId = modelId;
        this.modelName = modelName;
        this.modelBattery = modelBattery;
        this.modelBrand = modelBrand;
    }
}
