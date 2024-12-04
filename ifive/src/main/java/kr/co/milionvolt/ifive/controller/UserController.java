package kr.co.milionvolt.ifive.controller;

import kr.co.milionvolt.ifive.dto.UserVO;
import kr.co.milionvolt.ifive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/findId/{username}/{email}")
    public ResponseEntity<UserVO> findById(@PathVariable String username,
                                           @PathVariable String email){
       UserVO vo =  userService.findByID(username,email);
       ResponseEntity<UserVO> entity = new ResponseEntity<>(vo, HttpStatus.OK);
       return entity;
    }

    @GetMapping("/findPwd/{userId}") // 비밀번호 1페이지 기능 구현
    public ResponseEntity<String> findPwdByUserId(@PathVariable String userId){
        String email = userService.findPasswordByUserId(userId);
        ResponseEntity<String> entity = new ResponseEntity<>(email,HttpStatus.OK);
        return entity; // 이메일 반환.  -> vue에서 정규화 표현으로 어느정도 가리고 보여줄 예정.
        // 입력한 이메일이랑 맞는지 비교.
    }

    @GetMapping("/findPwd2/{username}/{email}")
    public ResponseEntity<UserVO> findPwd(@PathVariable String username,
                                          @PathVariable String email){
        UserVO vo =userService.findPass(username, email);
        ResponseEntity<UserVO> entity = new ResponseEntity<>(vo, HttpStatus.OK);
        return entity;
    }

    @PutMapping("/newPwd")
    @Transactional
    public ResponseEntity<String> newPwd(@RequestBody UserVO vo){
        userService.newPwd(vo);
        ResponseEntity<String> entity = new ResponseEntity<>("비밀번호 수정 완료",HttpStatus.OK);
        return entity;
    }
}
