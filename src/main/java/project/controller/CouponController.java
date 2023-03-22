package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.bussiness.service.CouponService;
import project.model.dto.response.CouponResponse;
import project.model.shopMess.Message;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/coupon")
@AllArgsConstructor
public class CouponController {
    private CouponService couponService;
        @GetMapping("/get-for-user")
    public ResponseEntity<?> getByUserId(){
        try {
            List<CouponResponse> responseList=couponService.getAllForClient();
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }catch (Exception exception){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
}
