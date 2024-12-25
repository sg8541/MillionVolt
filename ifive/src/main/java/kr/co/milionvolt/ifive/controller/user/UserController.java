package kr.co.milionvolt.ifive.controller.user;

import kr.co.milionvolt.ifive.domain.user.FindFwdDTO;
import kr.co.milionvolt.ifive.domain.user.FindIdDTO;
import kr.co.milionvolt.ifive.domain.user.ResetDTO;
import kr.co.milionvolt.ifive.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
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

    @PostMapping("/resetPwd")
    @Transactional
    public ResponseEntity<String> resetPwd(@RequestBody ResetDTO dto){
        userService.newPwd(dto);
        ResponseEntity<String> entity = new ResponseEntity<>("비밀번호 수정 완료",HttpStatus.OK);
        return entity;
    }

    @PostMapping("/exit/{id}")
    public ResponseEntity<?> exit(@PathVariable Integer id,
                                  @RequestBody Map<String, String> request) {
        String password = request.get("password");
        final int WITHDRAWAL_SUCCESS = 200;
        final int PASSWORD_MISMATCH = 400;
        int result = userService.exitUser(id, password);
        if(result == WITHDRAWAL_SUCCESS){
            return ResponseEntity.status(HttpStatus.OK).body("탈퇴 요청이 성공적으로 처리되었습니다.");
        }else if(result ==PASSWORD_MISMATCH){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호를 다시 확인해 주세요");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("회원탈퇴 중 문제가 발생하였습니다.");
        }

    }
}
