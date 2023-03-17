package project.bussiness.service;

import project.model.dto.request.ProductRequest;
import project.model.dto.response.ProductResponse;
import project.model.entity.Product;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductService extends RootService<Product,Integer, ProductRequest, ProductResponse> {
    List<ProductResponse> getFeatureProduct(LocalDateTime startDate, LocalDateTime endDate);
}
