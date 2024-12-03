package kr.co.milionvolt.ifive.controller;

import kr.co.milionvolt.ifive.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class EmailController {
    @Autowired
    private EmailService emailService;

    @GetMapping("/email/{email}")
    public ResponseEntity<Integer> display(@PathVariable String email){
        System.out.println(email);
        int num = emailService.setMailSend(email);

        ResponseEntity entity= new ResponseEntity(num, HttpStatus.OK);
        return entity;
    }

}
