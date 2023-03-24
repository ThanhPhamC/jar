package project.bussiness.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import project.model.dto.request.CartDetailRequest;
import project.model.dto.request.CartRequest;
import project.model.dto.response.CartResponse;
import project.model.dto.response.ProductReportByBrand;
import project.model.dto.response.ProductReportByCatalog;
import project.model.entity.Cart;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface CartService extends RootService<Cart,Integer, CartRequest, CartResponse> {
    List<Cart> findByCreatDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    ResponseEntity<?> addToCart(CartDetailRequest cartDetailRequest, String action);
   Page<CartResponse> findByStatusIn(Integer status, Pageable pageable);
   CartResponse showCartPending();
    Map<String,Object> getAllForClient(Pageable pageable);
    ResponseEntity<?> changeStatus(Integer cartId,Integer status);
//    ResponseEntity<?> getRevenueByBrand(String brand,LocalDateTime start,LocalDateTime end);
List<ProductReportByBrand> findCartByStatusAndCreatDateBetween(int status , int bradId, LocalDateTime createDate, LocalDateTime endDate);
}
