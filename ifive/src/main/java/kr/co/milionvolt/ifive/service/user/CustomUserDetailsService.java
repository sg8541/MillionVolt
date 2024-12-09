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

    // 기존 이메일 기반 메서드
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.debug("Attempting to load user by userId: {}", userId);
        UserDetailsVO detailsVO = userMapper.findByUser(userId);
        if(detailsVO == null) {
            log.error("User not found with userId: {}", userId);
            return (UserDetails) new UsernameNotFoundException("User not found with userId: " + userId);
        }

        log.info("User info : {} ", detailsVO.toString());

        return new org.springframework.security.core.userdetails.User(
                detailsVO.getUserId(),
                detailsVO.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(detailsVO.getRole()))
        );
    }
//        Member member = memberRepository.findByEmailWithRole(email)
//                .orElseThrow(() -> {
//                    log.error("User not found with email: {}", email);
//                    return new UsernameNotFoundException("User not found with email: " + email);
//                });
//
//        log.debug("User found: {}", member.getEmail());
//
//        return new org.springframework.security.core.userdetails.User(
//                member.getEmail(),
//                member.getPassword(),
//                Collections.singleton(new SimpleGrantedAuthority(member.getRole().getRoleName()))
//        );
//    }
//
//    // memberId 기반으로 사용자 로드하는 메서드
//    @Transactional
//    public UserDetails loadUserById(Integer memberId) throws UsernameNotFoundException {
        public UserDetails loadUserById(Integer id) throws UsernameNotFoundException {
            log.debug("Attempting to load user by memberId: {}", id);
            UserDetailsVO detailsVO = userMapper.findByUser(id);

            if (detailsVO == null) {
                log.error("User not found with id: {}", id);
                return (UserDetails) new UsernameNotFoundException("User not found with memberId: " + id);
            }

            return new org.springframework.security.core.userdetails.User(
                String.valueOf(detailsVO.getId()), // memberId를 username으로 사용
                detailsVO.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(detailsVO.getRole()))
            );
        }
//        log.debug("Attempting to load user by memberId: {}", memberId);
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> {
//                    log.error("User not found with memberId: {}", memberId);
//                    return new UsernameNotFoundException("User not found with memberId: " + memberId);
//                });
//
//        log.debug("User found: {}", member.getMemberId());
//
//        return new org.springframework.security.core.userdetails.User(
//                String.valueOf(member.getMemberId()), // memberId를 username으로 사용
//                member.getPassword(),
//                Collections.singleton(new SimpleGrantedAuthority(member.getRole().getRoleName()))
//        );
//    }
}
