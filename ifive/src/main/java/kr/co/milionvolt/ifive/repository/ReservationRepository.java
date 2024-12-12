package kr.co.milionvolt.ifive.repository;

import kr.co.milionvolt.ifive.entity.ReservationRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends CrudRepository<ReservationRedis,Integer> {

}
