package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.Users;
@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {
    Users findUsersByUserName(String userName);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Page<Users> findByUserNameContaining(String name, Pageable pageable);
    Users findByEmail(String email);
}
