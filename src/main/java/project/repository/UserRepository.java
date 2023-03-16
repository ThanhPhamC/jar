package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.Users;
@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {
    Users findUsersByUserName(String userName);
}
