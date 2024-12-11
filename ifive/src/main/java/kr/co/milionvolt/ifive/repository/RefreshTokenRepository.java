package kr.co.milionvolt.ifive.repository;

import kr.co.milionvolt.ifive.entity.RefreshTokenRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshTokenRedis, Integer> {
    void deleteById(Integer id);
}
