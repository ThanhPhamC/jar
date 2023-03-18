package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.dto.response.ProductResponse;
import project.model.entity.Catalog;
import project.model.entity.Product;

import java.util.List;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog,Integer> {
    List<Catalog> findByProductListIn(List<Product> listProduct);
}
