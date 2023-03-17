package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.bussiness.service.ProductService;
import project.model.dto.request.ProductRequest;
import project.model.dto.response.ProductResponse;
import project.model.entity.Cart;
import project.model.entity.CartDetail;
import project.model.entity.Product;
import project.repository.CartDetailRepository;
import project.repository.CartRepository;
import project.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductImpl implements ProductService {
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private CartDetailRepository cartDetailRepository;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        return null;
    }

    @Override
    public ProductResponse saveOrUpdate(ProductRequest productRequest) {
        return null;
    }

    @Override
    public ProductResponse update(Integer id, ProductRequest productRequest) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductResponse> getAllForClient() {
        List<ProductResponse> productResponseList = findAll().stream()
                .map(this::mapPoJoToResponse)
                .collect(Collectors.toList());
        return productResponseList;
    }

    @Override
    public Product findById(Integer id) {
        return productRepository.findById(id).get();
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public Product mapRequestToPoJo(ProductRequest productRequest) {
        return null;
    }

    @Override
    public ProductResponse mapPoJoToResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setProductImg(product.getProductImg());
        productResponse.setName(product.getName());
        productResponse.setStatus(product.getStatus());
        productResponse.setExportPrice(product.getExportPrice());
        productResponse.setDiscount(product.getDiscount());
        return productResponse;
    }

    @Override
    public List<ProductResponse> getFeatureProduct(LocalDateTime startDate, LocalDateTime endDate) {
        List<Cart> listCart = cartRepository.findByCreatDateBetween(startDate, endDate);
        List<CartDetail> listCartDetail = cartDetailRepository.findByCartIn(listCart);
        Map<Integer, Integer> maxMap = new HashMap<>();
        for (int i = 0; i < listCartDetail.size(); i++) {
            int quantity = listCartDetail.get(i).getQuantity();
            int key = listCartDetail.get(i).getProduct().getId();
            if (maxMap.containsKey(key)) {
                int value = maxMap.get(key);
                maxMap.put(key, value + quantity);
            } else {
                maxMap.put(key, quantity);
            }
        }

        Map<Integer, Integer> result = maxMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .skip(maxMap.size()-3)
                .limit(3)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));

        List<Product> listProduct = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
            listProduct.add(findById(entry.getKey()));
        }

        List<ProductResponse> responses = listProduct.stream()
                .map(this::mapPoJoToResponse)
                .collect(Collectors.toList());

        return responses;
    }
}
