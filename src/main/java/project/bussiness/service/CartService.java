package project.bussiness.service;

import org.springframework.http.ResponseEntity;
import project.model.dto.request.CartDetailRequest;
import project.model.dto.request.CartRequest;
import project.model.dto.response.CartResponse;
import project.model.entity.Cart;

import java.time.LocalDateTime;
import java.util.List;

public interface CartService extends RootService<Cart,Integer, CartRequest, CartResponse> {
    List<Cart> findByCreatDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    ResponseEntity<?> addToCart(CartDetailRequest cartDetailRequest, String action);

}
