package project.bussiness.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.bussiness.service.CartService;
import project.model.dto.request.CartRequest;
import project.model.dto.response.CartResponse;
import project.model.entity.Cart;
import project.repository.CartRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CartImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
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
        return null;
    }

    @Override
    public List<Cart> findByCreatDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return cartRepository.findByCreatDateBetween(startDate, endDate);
    }
}
