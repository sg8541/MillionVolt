package kr.co.milionvolt.ifive.controller.reservation;

import kr.co.milionvolt.ifive.service.reservation.ReserverNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ReserverNameController {

    @Autowired
    ReserverNameService reserverNameService;

    @GetMapping("/reservation/{id}")
    public ResponseEntity<?> reserveName(@PathVariable int id) {
        try {
            String username = reserverNameService.reserveName(id);
            if (username != null) {
                return ResponseEntity.ok().body(Map.of("username", username));
            } else {
                return ResponseEntity.status(404).body("회원을 찾지 못했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("서버에서 오류가 발생했습니다.");
        }
    }
}
