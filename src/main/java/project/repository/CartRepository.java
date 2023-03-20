package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.Cart;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
    List<Cart> findByCreatDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    Cart findByUsers_UserIdAndStatus(int userId, int stautus);
    Page<Cart> findByStatus(Integer status,Pageable pageable);
    List<Cart> findByStatusAndUsers_UserId(Integer status, Integer userId);
}
