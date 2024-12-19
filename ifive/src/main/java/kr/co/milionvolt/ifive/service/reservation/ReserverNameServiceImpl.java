package kr.co.milionvolt.ifive.service.reservation;

import kr.co.milionvolt.ifive.mapper.ReserverNameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReserverNameServiceImpl implements ReserverNameService {

    @Autowired
    ReserverNameMapper reserverNameMapper;

    @Override
    public String reserveName(int id) {
        return reserverNameMapper.findUserId(id);
    }
}
