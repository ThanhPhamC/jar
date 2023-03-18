package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.bussiness.service.ProductService;
import project.model.dto.request.ProductRequest;
import project.model.dto.response.ProductResponse;
import project.model.entity.Cart;
import project.model.entity.CartDetail;
import project.model.entity.Product;
import project.model.entity.Review;
import project.repository.CartDetailRepository;
import project.repository.CartRepository;
import project.repository.ProductRepository;
import project.repository.BrandRepository;
import project.repository.CatalogRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductImpl implements ProductService {
    private CartRepository cartRepository;
    private CartDetailRepository cartDetailRepository;
    private ProductRepository productRepo;
    private CatalogRepository catalogRepo;
    private BrandRepository brandRepo;

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
        return productRepo.findAll();
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
        return productRepo.findById(id).get();
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public List<ProductResponse> getFeatureProduct(LocalDateTime startDate, LocalDateTime endDate, int size) {
        List<Cart> listCart = cartRepository.findByCreatDateBetween(startDate, endDate);
        List<Cart> listCartFilter = listCart.stream().filter(cart -> cart.getStatus() == 1)
                .collect(Collectors.toList());
        List<CartDetail> listCartDetail = cartDetailRepository.findByCartIn(listCartFilter);
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
                .skip(maxMap.size() - size)
                .limit(size)
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

    @Override
    public List<Product> findByCartDetailListIn(List<CartDetail> listCartDetail) {
        return productRepo.findByCartDetailListIn(listCartDetail);
    }

    @Override
    public List<ProductResponse> getTopRatedProduct(LocalDateTime startDate, LocalDateTime endDate, int size) {
        List<Cart> listCart = cartRepository.findByCreatDateBetween(startDate, endDate);
        List<Cart> listCartFilter = listCart.stream().filter(cart -> cart.getStatus() == 1)
                .collect(Collectors.toList());
        List<CartDetail> listCartDetail = cartDetailRepository.findByCartIn(listCartFilter);
        List<Product> listProduct = findByCartDetailListIn(listCartDetail);
        List<ProductResponse> listProductRes = listProduct.stream()
                .map(this::mapPoJoToResponse).collect(Collectors.toList());
        List<ProductResponse> responses = listProductRes.stream()
                .sorted(Comparator.comparingDouble(response -> response.getStarPoint()))
                .skip(listProductRes.size()-size)
                .limit(size)
                .collect(Collectors.toList());
        return responses;
    }

    @Override
    public int countByCatalog_Id(int catalogId) {
        return productRepo.countByCatalog_Id(catalogId);
    }


    @Override
    public Product mapRequestToPoJo(ProductRequest productRequest) {
        Product product = new Product();
        if (productRequest.getStatus() > 0) {
            product.setStatus(productRequest.getStatus());
        } else {
            product.setStatus(1);
        }
        product.setName(productRequest.getName());
        product.setCreatDate(productRequest.getCreatDate());
        product.setDiscount(productRequest.getDiscount());
        product.setExportPrice(productRequest.getExportPrice());
        product.setImportPrice(productRequest.getImportPrice());
        product.setProductDescriptions(productRequest.getProductDescriptions());
        product.setProductImg(productRequest.getProductImg());
        product.setProductQuantity(productRequest.getProductQuantity());
        product.setTitle(productRequest.getTitle());
        product.setCatalog(catalogRepo.findById(productRequest.getCatalogId()).get());
        product.setBrand(brandRepo.findById(productRequest.getBrandId()).get());
        return product;
    }

    @Override
    public ProductResponse mapPoJoToResponse(Product pro) {
        ProductResponse rp = new ProductResponse();
        rp.setId(pro.getId());
        rp.setName(pro.getName());
        rp.setStatus(pro.getStatus());
        rp.setCreatDate(pro.getCreatDate());
        rp.setDiscount(pro.getDiscount());
        rp.setExportPrice(pro.getExportPrice());
        rp.setImportPrice(pro.getImportPrice());
        rp.setProductDescriptions(pro.getProductDescriptions());
        rp.setProductImg(pro.getProductImg());
        rp.setProductQuantity(pro.getProductQuantity());
        rp.setTitle(pro.getTitle());
        rp.setBrandName(pro.getBrand().getName());
        rp.setCatalogName(pro.getCatalog().getName());
//        rp.setReviewList(pro.getReviewList());
        rp.setSubImgList(pro.getSubImgList());
        List<Integer> list = new ArrayList<>();
        for (Review rw : pro.getReviewList()) {
            list.add(rw.getStarPoint());
        }
        double average = list.stream().mapToDouble(num -> num).average().orElse(0.0);
        rp.setStarPoint(average);
        return rp;
    }

    @Override
    public List<ProductResponse> topNewProduct() {
        List<ProductResponse> responses = productRepo.findAll().stream().sorted(Comparator.comparing(Product::getCreatDate)).map(this::mapPoJoToResponse).collect(Collectors.toList());
        List<ProductResponse> result = responses.stream()
                .skip(Math.max(0, responses.size()) - 3)
                .filter(Objects::nonNull).collect(Collectors.toList());
        return result;
    }
}
