package kr.co.milionvolt.ifive.service;

import kr.co.milionvolt.ifive.model.UserVO;

public interface UserService {
    public UserVO findByID(String username, String email);
    public String findPasswordByUserId(String userId);
    public UserVO findPass(String username, String email);
    public void newPwd(UserVO vo);
}
