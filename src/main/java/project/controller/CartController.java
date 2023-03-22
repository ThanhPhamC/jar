package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.CartDetailService;
import project.bussiness.service.CartService;
import project.model.dto.request.CartDetailRequest;
import project.model.dto.request.CartRequest;
import project.model.dto.response.CartResponse;
import project.model.shopMess.Message;
import project.repository.TokenLogInReposirory;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/cart")
@AllArgsConstructor
public class CartController {
    private CartService cartService;
    private CartDetailService detailService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> addToCart(@RequestBody CartDetailRequest cartDetailRequest, @RequestParam String action){
        return cartService.addToCart(cartDetailRequest, action);
    }
    @PutMapping("/delete-cartDetail")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> deleteCartDetail(@RequestParam Integer cartDetailId){
        return detailService.delete(cartDetailId);
    }
    @GetMapping("/get_cart_pending_for_user")
    public ResponseEntity<?>getCartPending(){
        try {
            CartResponse response= cartService.showCartPending();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @PutMapping("/checkout")
    public ResponseEntity<?> checkOutCart(@RequestParam CartRequest rq){
        return null;
    }
}
