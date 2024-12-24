package kr.co.milionvolt.ifive.controller.penalty;

import jdk.jfr.RecordingState;
import kr.co.milionvolt.ifive.service.penalty.PenaltyService;
import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.http.HTTP;

@RestController
@RequestMapping("/api/v1")
public class PenaltyController {

    @Autowired
    private PenaltyService penaltyService;

    @GetMapping("/penalty/{reservationId}")
    public ResponseEntity<Integer> sendPenayltAmount(@PathVariable int reservationId){
        ResponseEntity<Integer> entity = null;
        try{
            int num = penaltyService.selectPenaltyAmount(reservationId);
            entity  = new ResponseEntity<>(num, HttpStatus.OK);
        }catch (BindingException e){
            entity = new ResponseEntity<>(0 , HttpStatus.OK);
        }
        return entity;
    }
}
