package project.bussiness.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.bussiness.service.CartDetailService;
import project.model.dto.request.CartDetailRequest;
import project.model.dto.response.CartDetailResponse;
import project.model.entity.Cart;
import project.model.entity.CartDetail;
import project.repository.CartDetailRepository;

import java.util.List;
import java.util.Map;

@Service
public class CartDetailImpl implements CartDetailService {
    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        return null;
    }

    @Override
    public CartDetailResponse saveOrUpdate(CartDetailRequest cartDetailRequest) {
        return null;
    }

    @Override
    public CartDetailResponse update(Integer id, CartDetailRequest cartDetailRequest) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public List<CartDetail> findAll() {
        return null;
    }

    @Override
    public List<CartDetailResponse> getAllForClient() {
        return null;
    }

    @Override
    public CartDetail findById(Integer id) {
        return null;
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public CartDetail mapRequestToPoJo(CartDetailRequest cartDetailRequest) {
        return null;
    }

    @Override
    public CartDetailResponse mapPoJoToResponse(CartDetail cartDetail) {
        return null;
    }

    @Override
    public List<CartDetail> findByCartIn(List<Cart> listCart) {
        return cartDetailRepository.findByCartIn(listCart);
    }
}
