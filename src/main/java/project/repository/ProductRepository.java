package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.CartDetail;
import project.model.entity.Product;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCartDetailListIn(List<CartDetail> listCartDetail);
    Page<Product> findByNameContaining(String searchName, Pageable pageable);
}
