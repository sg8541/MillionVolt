package kr.co.milionvolt.ifive.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class EmailService {
    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "gjsdms1244@gmail.com";

    public int setMailSend(String email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email); // 받는 메일
        message.setFrom(EmailService.FROM_ADDRESS); //보내는 이메일
        message.setSubject("백만볼트 인증번호"); // 제목

        Random random = new Random();
        int sixDigitNumber = 100000 + random.nextInt(900000);
        String strNumber = String.valueOf(sixDigitNumber);

        message.setText("백만볼트 이메일 인증번호 6자리 : "+strNumber); // 내용.
        mailSender.send(message);

        return sixDigitNumber;
    }
}
