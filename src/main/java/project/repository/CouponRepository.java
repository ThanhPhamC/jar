package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.model.entity.Coupon;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon,Integer> {
    List<Coupon> findByStatusAndUsers_UserId(Integer status,Integer userId);
}
