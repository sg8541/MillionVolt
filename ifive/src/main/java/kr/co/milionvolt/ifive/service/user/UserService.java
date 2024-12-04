package kr.co.milionvolt.ifive.service.user;

import kr.co.milionvolt.ifive.domain.user.FindFwdDTO;
import kr.co.milionvolt.ifive.domain.user.FindIdDTO;
import kr.co.milionvolt.ifive.domain.user.UserVO;
import kr.co.milionvolt.ifive.domain.user.ResetDTO;

import java.util.List;

public interface UserService {
    // 유저 전체 조회
    List<UserVO> findAll();
    public FindIdDTO findByID(String username, String email);
    public String findPasswordByUserId(String userId);
    public FindFwdDTO findPass(String username, String email);
    public void newPwd(ResetDTO dto);
}
