package kr.co.milionvolt.ifive.entity;

import lombok.*;
import org.apache.ibatis.annotations.Many;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@RedisHash("Reservation")
public class ReservationRedis implements Serializable {
    private static final long serialVersionUID =1L;

    @Id
    private Integer reservationId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private int userId;
    private int stationId;
    private int chargerId;

    @TimeToLive
    private Long ttl;

}
