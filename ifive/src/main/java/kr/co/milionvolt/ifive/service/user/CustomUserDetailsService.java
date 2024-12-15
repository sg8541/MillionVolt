package kr.co.milionvolt.ifive.service.user;

import kr.co.milionvolt.ifive.domain.user.UserDetailsVO;
import kr.co.milionvolt.ifive.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.debug("Attempting to load user by userId: {}", userId);
        UserDetailsVO detailsVO = userMapper.findByUserId(userId);
        if (detailsVO == null) {
            log.error("User not found with userId: {}", userId);
            throw new UsernameNotFoundException("User not found with userId: " + userId);
        }

        log.info("User info : {} ", detailsVO.toString());

        return new org.springframework.security.core.userdetails.User(
                detailsVO.getUserId(),
                detailsVO.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(detailsVO.getRole()))
        );
    }

    public UserDetails loadUserById(Integer id) throws UsernameNotFoundException {
        log.debug("Attempting to load user by id: {}", id);
        UserDetailsVO detailsVO = userMapper.findById(id);

        if (detailsVO == null) {
            log.error("User not found with id: {}", id);
            throw new UsernameNotFoundException("User not found with id: " + id);
        }

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(detailsVO.getId()), // memberId를 username으로 사용
                detailsVO.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(detailsVO.getRole()))
        );
    }
}
