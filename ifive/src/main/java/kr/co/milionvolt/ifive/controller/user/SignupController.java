package kr.co.milionvolt.ifive.controller.user;

import kr.co.milionvolt.ifive.domain.user.SignupDTO;
import kr.co.milionvolt.ifive.service.user.SignupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class SignupController {

    private final SignupService signupService;


    @PostMapping("/signup")
    public ResponseEntity<?> signup (@RequestBody SignupDTO signupDTO){
        log.info("========================: " + signupDTO.toString());
        try{
            boolean success = signupService.signup(signupDTO);
            if(success) {
                return ResponseEntity.status(HttpStatus.OK).body("회원가입이 성공적으로 완료되었습니다.");
            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("회원가입에 실패하였습니다.");
            }
        } catch (Exception e) {
            log.info("error : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 중 문제가 발생하였습니다.");
        }
    }

    @GetMapping("/email-check")
    public ResponseEntity<?> checkEmailDuplicate(@RequestParam String email){
        final int NOT_FIUND = 0;
        log.info("email : {}", email);

        int result = signupService.checkEmailDuplicate(email);
        if(result == NOT_FIUND) {
            return ResponseEntity.status(HttpStatus.OK).body("사용 가능한 이메일입니다.");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용할 수 없는 이메일입니다.");
        }
    }
    
    // 아이디 중복 검사
    @GetMapping("/id-check")
    public ResponseEntity<?> checkIdDuplicate(@RequestParam("user_id") String userId){
        final int NOT_FIUND = 0;
        
        log.info("중복 검사 id : {}", userId);
        int result = signupService.checkIdDuplicate(userId);
        if(result == NOT_FIUND) {
            return ResponseEntity.status(HttpStatus.OK).body("사용 가능한 아이디입니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용할 수 없는 아이디입니다.");
        }
        
    }





}
