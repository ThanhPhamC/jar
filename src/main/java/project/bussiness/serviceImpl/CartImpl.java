package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.bussiness.service.CartDetailService;
import project.bussiness.service.CartService;
import project.model.dto.request.CartDetailRequest;
import project.model.dto.request.CartRequest;
import project.model.dto.response.CartDetailResponse;
import project.model.dto.response.CartResponse;
import project.model.entity.Cart;
import project.model.entity.CartDetail;
import project.model.entity.Product;
import project.model.entity.Users;
import project.model.shopMess.Message;
import project.repository.CartDetailRepository;
import project.repository.CartRepository;
import project.repository.ProductRepository;
import project.repository.UserRepository;
import project.security_jwt.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartImpl implements CartService {
    private CartRepository cartRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private CartDetailRepository cartDetailRepository;
    private CartDetailService cartDetailService;


    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        return null;
    }

    @Override
    public CartResponse saveOrUpdate(CartRequest cartRequest) {
        return null;
    }

    @Override
    public CartResponse update(Integer id, CartRequest cartRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        return null;
    }

    @Override
    public List<Cart> findAll() {
        return null;
    }

    @Override
    public List<CartResponse> getAllForClient() {
        return null;
    }

    @Override
    public Cart findById(Integer id) {
        return null;
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public Cart mapRequestToPoJo(CartRequest cartRequest) {
        return null;
    }

    @Override
    public CartResponse mapPoJoToResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setName(cart.getName());
        response.setStatus(cart.getStatus());
        List<CartDetailResponse> responseList=cart.getCartDetailList().stream().map(cartDetail -> {
            CartDetailResponse rp= cartDetailService.mapPoJoToResponse(cartDetail);
            return rp;
        }).collect(Collectors.toList());
        response.setDetailResponses(responseList);
        return response;
    }

    @Override
    public List<Cart> findByCreatDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return cartRepository.findByCreatDateBetween(startDate, endDate);
    }

    @Override
    public ResponseEntity<?> addToCart(CartDetailRequest cartDetailRequest, String action) {
        CustomUserDetails userIsLoggingIn = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userIsLoggingIn.getUsername());
        Product product = productRepository.findById(cartDetailRequest.getProductId()).get();
        Cart cart = cartRepository.findByUsers_UserIdAndStatus(users.getUserId(), 0);
        CartDetail cartDetail = cartDetailRepository.findByProduct_IdAndCart_Id(product.getId(), cart.getId());

        try {
            if (cartDetail != null) {
                if (action.equals("create")) {
                    cartDetail.setQuantity(cartDetail.getQuantity() + cartDetailRequest.getQuantity());
                    cartDetail.setPrice(product.getExportPrice() * cartDetail.getQuantity());
                    cartDetailRepository.save(cartDetail);
                } else if (action.equals("edit")) {
                    cartDetail.setQuantity(cartDetailRequest.getQuantity());
                    cartDetail.setPrice(product.getExportPrice() * cartDetail.getQuantity());
                    cartDetailRepository.save(cartDetail);
                }
                return ResponseEntity.ok().body(Message.ADD_TO_CART_SUCCESS);
            } else {
                CartDetail cartDetailNew = new CartDetail();
                cartDetailNew.setProduct(product);
                cartDetailNew.setCart(cart);
                cartDetailNew.setQuantity(cartDetailRequest.getQuantity());
                cartDetailNew.setPrice(product.getExportPrice() * cartDetailRequest.getQuantity());
                cartDetailNew.setName(product.getName());
                cartDetailNew.setStatus(1);
                cartDetailRepository.save(cartDetailNew);
                return ResponseEntity.ok().body(Message.ADD_TO_CART_SUCCESS);
            }
        } catch (Exception e){
            return  ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
}
