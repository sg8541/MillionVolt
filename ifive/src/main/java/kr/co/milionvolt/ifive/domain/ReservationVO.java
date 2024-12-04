package kr.co.milionvolt.ifive.domain;

import lombok.Getter;

@Getter
public class ReservationVO {
    private Integer reservationId;
    private Integer chargerId;
    private ReservationDTO.Status status;

    public ReservationVO(Integer reservationId, Integer chargerId, ReservationDTO.Status status) {
        this.reservationId = reservationId;
        this.chargerId = chargerId;
        this.status = status;
    }
}
