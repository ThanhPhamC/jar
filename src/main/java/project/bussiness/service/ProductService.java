package project.bussiness.service;

import project.model.dto.request.ProductRequest;
import project.model.dto.response.ProductResponse;
import project.model.entity.CartDetail;
import project.model.entity.Location;
import project.model.entity.Product;
import java.time.LocalDateTime;
import java.util.List;

public interface ProductService extends RootService<Product,Integer, ProductRequest, ProductResponse> {
    List<ProductResponse> topNewProduct();
    List<ProductResponse> getFeatureProduct(LocalDateTime startDate, LocalDateTime endDate, int size);
    List<Product> findByCartDetailListIn(List<CartDetail> listCartDetail);
    List<ProductResponse> getTopRatedProduct(LocalDateTime startDate, LocalDateTime endDate, int size);
    int countByCatalog_Id(int catalogId);
    List<ProductResponse> filterProductByPriceLocationStar(List<Integer> listLocationId, float max, float min, List<Integer> starPoint);
}
