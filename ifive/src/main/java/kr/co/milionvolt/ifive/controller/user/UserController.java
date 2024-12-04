package kr.co.milionvolt.ifive.controller.user;

import kr.co.milionvolt.ifive.domain.user.FindFwdDTO;
import kr.co.milionvolt.ifive.domain.user.FindIdDTO;
import kr.co.milionvolt.ifive.domain.user.ResetDTO;
import kr.co.milionvolt.ifive.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/findId/{username}/{email}")
    public ResponseEntity<FindIdDTO> findById(@PathVariable String username,
                                              @PathVariable String email){
        FindIdDTO dto =  userService.findByID(username,email);

       ResponseEntity<FindIdDTO> entity = new ResponseEntity<>(dto, HttpStatus.OK);
       return entity;
    }

    @GetMapping("/findPwd/{userId}") // 비밀번호 1페이지 기능 구현
    public ResponseEntity<String> findPwdByUserId(@PathVariable String userId){
        String email = userService.findPasswordByUserId(userId);
        ResponseEntity<String> entity = new ResponseEntity<>(email,HttpStatus.OK);

        return entity; // 이메일 반환.  -> vue에서 정규화 표현으로 어느정도 가리고 보여줄 예정.
        // 입력한 이메일이랑 맞는지 비교.
    }

    @GetMapping("/findPwd/{username}/{email}")
    public ResponseEntity<FindFwdDTO> findPwd(@PathVariable String username,
                                              @PathVariable String email){
        FindFwdDTO dto =userService.findPass(username, email);
        ResponseEntity<FindFwdDTO> entity = new ResponseEntity<>(dto, HttpStatus.OK);
        return entity;
    }

    @PutMapping("/resetPwd")
    @Transactional
    public ResponseEntity<String> resetPwd(@RequestBody ResetDTO dto){
        userService.newPwd(dto);
        ResponseEntity<String> entity = new ResponseEntity<>("비밀번호 수정 완료",HttpStatus.OK);
        return entity;
    }
}
