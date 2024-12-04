package kr.co.milionvolt.ifive.service;

import kr.co.milionvolt.ifive.domain.UserVO;

import java.util.List;

public interface UserService {
    // 유저 전체 조회
    List<UserVO> findAll();
}
