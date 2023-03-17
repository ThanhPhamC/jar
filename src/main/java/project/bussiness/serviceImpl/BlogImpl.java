package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.bussiness.service.BlogService;
import project.model.dto.request.BlogRequest;
import project.model.dto.response.BlogResponse;
import project.model.entity.Blog;
import project.repository.BlogRepository;
import project.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BlogImpl implements BlogService {
     private BlogRepository blogRepo;
     private UserRepository userRepo;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        return null;
    }

    @Override
    public BlogResponse saveOrUpdate(BlogRequest blogRequest) {
        return null;
    }

    @Override
    public BlogResponse update(Integer id, BlogRequest blogRequest) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public List<Blog> findAll() {
        return null;
    }

    @Override
    public List<BlogResponse> getAllForClient() {
        return null;
    }

    @Override
    public Blog findById(Integer id) {
        return null;
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public Blog mapRequestToPoJo(BlogRequest rq) {
        Blog blog = new Blog();
        blog.setName(rq.getName());
        blog.setContent(rq.getContent());
        blog.setCreatDate(rq.getCreatDate());
        blog.setBlogImg(rq.getBlogImg());
        blog.setStatus(rq.getStatus());
        blog.setUsers(userRepo.findById(rq.getUserId()).get());
        if (blog.getUsers()==null){
            return null;
        }
        return blog;
    }
    @Override
    public BlogResponse mapPoJoToResponse(Blog blog) {
        BlogResponse response = new BlogResponse();
        response.setId(blog.getId());
        response.setName(blog.getName());
        response.setCreatDate(blog.getCreatDate());
        response.setUserName(blog.getUsers().getUserName());
        response.setStatus(blog.getStatus());
        response.setContent(blog.getContent());
        response.setBlogImg(blog.getBlogImg());
        return  response;
    }

    @Override
    public List<BlogResponse> getTopNew() {
        List<BlogResponse> responses = blogRepo.findAll().stream().sorted(Comparator.comparing(Blog::getCreatDate)).map(this::mapPoJoToResponse).collect(Collectors.toList());
        List<BlogResponse> result=responses.stream().skip(Math.max(0, responses.size())-3).collect(Collectors.toList());
        return result;
    }
}
