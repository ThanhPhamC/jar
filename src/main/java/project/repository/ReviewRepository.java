package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Integer> {
}
