package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.FlashSale;
@Repository
public interface FlashSaleRepository extends JpaRepository<FlashSale,Integer> {
}
