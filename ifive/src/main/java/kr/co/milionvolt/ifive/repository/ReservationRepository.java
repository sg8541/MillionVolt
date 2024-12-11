package kr.co.milionvolt.ifive.repository;

import kr.co.milionvolt.ifive.entity.ReservationRedis;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<ReservationRedis,Integer> {


}
