package kr.co.milionvolt.ifive.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@RedisHash("Reservation")
public class ReservationRedis implements Serializable {
    private static final long serialVersionUID =1L;

    @Id
    private Integer reservationId;

    private Timestamp startTime;
    private Timestamp endTime;
    private String userId;

    @TimeToLive
    private Long ttl;

}
