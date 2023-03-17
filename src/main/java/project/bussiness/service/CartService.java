package project.bussiness.service;

import project.model.dto.request.CartRequest;
import project.model.dto.response.CartResponse;
import project.model.entity.Cart;

import java.time.LocalDateTime;
import java.util.List;

public interface CartService extends RootService<Cart,Integer, CartRequest, CartResponse> {
    List<Cart> findByCreatDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
