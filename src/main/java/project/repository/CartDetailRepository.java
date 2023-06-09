package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.Cart;
import project.model.entity.CartDetail;

import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail,Integer> {
    List<CartDetail> findByCartIn(List<Cart> listCart);
    List<CartDetail> findByProduct_IdAndCart_Id(int productId, int cartId);
    List<CartDetail> findByCart_Id(Integer cartId);
    CartDetail findByProduct_Id(int productId);

}
