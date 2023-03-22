package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.bussiness.service.BrandService;
import project.bussiness.service.ProductService;
import project.model.dto.request.BrandRequest;
import project.model.dto.response.BrandResponse;
import project.model.dto.response.ProductResponse;
import project.model.entity.Brand;
import project.model.entity.Product;
import project.repository.BrandRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BrandImpl implements BrandService {
    private BrandRepository brandRepository;
    private ProductService productService;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        return null;
    }

    @Override
    public BrandResponse saveOrUpdate(BrandRequest brandRequest) {
        return null;
    }

    @Override
    public BrandResponse update(Integer id, BrandRequest brandRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        return null;
    }

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public List<BrandResponse> getAllForClient() {
        return null;
    }

    @Override
    public Brand findById(Integer id) {
        return brandRepository.findById(id).get();
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public Brand mapRequestToPoJo(BrandRequest brandRequest) {
        return null;
    }

    @Override
    public BrandResponse mapPoJoToResponse(Brand brand) {
        BrandResponse brandResponse = new BrandResponse();
        brandResponse.setId(brand.getId());
        brandResponse.setName(brand.getName());
        brandResponse.setBrandLogo(brand.getBrandLogo());
        brandResponse.setStatus(brand.getStatus());
        return brandResponse;
    }

    @Override
    public List<BrandResponse> getFeatureBrand(LocalDateTime startDate, LocalDateTime endDate, int size) {
        List<ProductResponse> productResponseList = productService.getFeatureProduct(startDate, endDate, size);
        List<Product> productList = new ArrayList<>();
        for (ProductResponse pro : productResponseList) {
            productList.add(productService.findById(pro.getId()));
        }
        Set<Brand> brandList = brandRepository.findByProductListIn(productList);
        List<BrandResponse> responses = brandList.stream()
                .map(this::mapPoJoToResponse).collect(Collectors.toList());
        return responses;
    }
}
