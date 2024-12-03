package kr.co.milionvolt.ifive.controller.user;

import kr.co.milionvolt.ifive.dto.user.SignUpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/signup")
public class SignUpController {

    @Autowired


    // 회원가입
    @PostMapping
    public ResponseEntity<?> singUp(@RequestBody SignUpDTO signUp){


        return ResponseEntity.ok().body(signUp);
    }


}
