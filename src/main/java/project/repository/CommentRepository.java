package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.CommentBlog;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentBlog,Integer> {

    List<CommentBlog> findAllByUsers_UserId(int Id);
    List<CommentBlog> findAllByBlog_Id(int Id);
}
