package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.bussiness.service.CommentService;
import project.model.dto.request.CommentRequest;
import project.model.dto.response.CommentResponse;
import project.model.entity.CommentBlog;
import project.model.entity.Users;
import project.repository.BlogRepository;
import project.repository.CommentRepository;
import project.repository.UserRepository;
import project.security_jwt.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CommentBlogServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        return null;
    }

    @Override
    public CommentResponse saveOrUpdate(CommentRequest commentRequest) {
        CommentBlog commentBlog = mapRequestToPoJo(commentRequest);
        return mapPoJoToResponse(commentRepository.save(commentBlog));
    }

    @Override
    public CommentResponse update(Integer id, CommentRequest commentRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {

        return null;
    }

    @Override
    public List<CommentBlog> findAll() {
        return null;
    }

    @Override
    public List<CommentResponse> getAllForClient() {
        return null;
    }

    @Override
    public CommentBlog findById(Integer id) {
        return null;
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public CommentBlog mapRequestToPoJo(CommentRequest commentRequest) {
        CustomUserDetails userIsLoggingIn = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userIsLoggingIn.getUsername());
        CommentBlog commentBlog = new CommentBlog();
        commentBlog.setContent(commentRequest.getContent());
        LocalDateTime now = LocalDateTime.now();
        commentBlog.setCreateDate(now);
        commentBlog.setBlog(blogRepository.findById(commentRequest.getBlogId()).get());
        commentBlog.setUsers(users);
        commentBlog.setStatus(0);
        return commentBlog;
    }
    @Override
    public CommentResponse mapPoJoToResponse(CommentBlog commentBlog) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(commentBlog.getId());
        commentResponse.setName(commentBlog.getName());
        commentResponse.setStatus(commentBlog.getStatus());
        commentResponse.setContent(commentBlog.getContent());
        commentResponse.setCreateDate(commentBlog.getCreateDate());
        commentResponse.setUserId(commentBlog.getUsers().getUserId());
        return commentResponse;
    }
}
