package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.Cart;
import project.model.entity.CartDetail;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
    List<Cart> findByCreatDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    Cart findByUsers_UserIdAndStatus(int userId, int stautus);
    Page<Cart> findByStatus(Integer status,Pageable pageable);
    Page<Cart> findByUsers_UserIdAndStatusIsNot(int users_userId, int status, Pageable pageable);
    List<Cart> findByStatusInAndUsers_UserId(List<Integer> status, Integer userId);
    List<Cart>  findByStatusAndUsers_UserIdAndCreatDateBetween(int status, int users_userId, LocalDateTime creatDate, LocalDateTime creatDate2 );
    List<Cart> findByStatusAndCityAndCreatDateBetween(Integer status, String city, LocalDateTime start, LocalDateTime end);
}
