package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.bussiness.service.ProductService;
import project.model.dto.request.ProductRequest;
import project.model.dto.response.ProductResponse;
import project.model.entity.Brand;
import project.model.entity.Product;
import project.repository.BrandRepository;
import project.repository.CatalogRepository;
import project.repository.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductImpl implements ProductService {
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
    public List<ProductResponse> findAll() {
        List<ProductResponse> responses = productRepo.findAll().stream().map(this::mapPoJoToResponse).collect(Collectors.toList());
        return responses;
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
        rp.setReviewList(pro.getReviewList());
        rp.setSubImgList(pro.getSubImgList());
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
