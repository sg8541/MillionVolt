package kr.co.milionvolt.ifive.domain;

import lombok.Getter;

@Getter
public class ChargerVO {
    private final Integer chargerId;
    private final ChargerDTO.ChagerType chagerType;
    private final ChargerDTO.ChargerStatus chargerStatus;

    public ChargerVO(Integer chargerId, ChargerDTO.ChagerType chagerType, ChargerDTO.ChargerStatus chargerStatus) {
        this.chargerId = chargerId;
        this.chagerType = chagerType;
        this.chargerStatus = chargerStatus;
    }

    public String getChagerTypeDisplay() {
        return chagerType.getDisplayValue();  // 출력 시 getDisplayValue() 사용
    }
}
