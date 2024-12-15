package kr.co.milionvolt.ifive.domain.reservation;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CancelReservationDTO {
    int userId;
    int reservationId;
}
