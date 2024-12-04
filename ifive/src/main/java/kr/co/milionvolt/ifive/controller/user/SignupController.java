package kr.co.milionvolt.ifive.controller.user;

import kr.co.milionvolt.ifive.domain.user.SignupDTO;
import kr.co.milionvolt.ifive.service.user.SignupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/signup")
public class SignupController {

    private final SignupService signupService;

    @PostMapping
    public ResponseEntity<?> signup (@RequestBody SignupDTO signupDTO){
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



}
