package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.model.entity.Users;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {
    Users findUsersByUserName(String userName);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Page<Users> findByUserNameContaining(String name, Pageable pageable);
    Users findByEmail(String email);
//    @Query(value = "select * from users  where firstName like '%' or lastName like '%'",nativeQuery = true)
//    List<Users> searchByFirstNameOrLastName(@Param("name") String name);
}
