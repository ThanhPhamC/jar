package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.bussiness.service.BlogService;
import project.model.dto.request.BlogRequest;
import project.model.dto.response.BlogResponse;
import project.model.entity.Blog;
import project.model.shopMess.Message;
import project.model.utility.Utility;
import project.repository.BlogRepository;
import project.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        Page<Blog> blogPage=blogRepo.findAll(pageable);
        Map<String,Object> result= Utility.returnResponse(blogPage);
        return result;
    }

    @Override
    public BlogResponse saveOrUpdate(BlogRequest blogRequest) {
            Blog blog= mapRequestToPoJo(blogRequest);
            Blog blog1 =blogRepo.save(blog);
            BlogResponse blogResponse =mapPoJoToResponse(blog1);

        return blogResponse;
    }

    @Override
    public BlogResponse update(Integer id, BlogRequest blogRequest) {
        Blog blog = mapRequestToPoJo(blogRequest);
        blog.setId(id);
        Blog blogUpdate = blogRepo.save(blog);
        BlogResponse blogResponse = mapPoJoToResponse(blogUpdate);
        return blogResponse;
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        try{
            Blog blogDelete = blogRepo.findById(id).get();
            blogDelete.setStatus(0);
            blogRepo.save(blogDelete);
            return ResponseEntity.ok().body(Message.SUCCESS);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }

    }


    @Override
    public List<Blog> findAll() {
        List<Blog> blogList =blogRepo.findAll();

        return blogList;
    }

    @Override
    public List<BlogResponse> getAllForClient() {
        List<BlogResponse> blogResponses= blogRepo.findAll().stream().map(this::mapPoJoToResponse).collect(Collectors.toList());
//        List<Blog>listBlog = blogRepo.findAll();
//        List<BlogResponse>blogResponseList=new ArrayList<>();
//
//        for (Blog b:listBlog) {
//            BlogResponse blogResponse=new BlogResponse();
//            blogResponse.setBlogImg(b.getBlogImg());
//            blogResponse.setName(b.getName());
//            blogResponse.setContent(b.getContent());
//            blogResponse.setStatus(b.getStatus());
//            blogResponse.setCreatDate(b.getCreatDate());
//            blogResponse.setUserName(b.getName());
//            blogResponse.setId(b.getId());
//            blogResponseList.add(blogResponse);
//        }
//        return blogResponseList;
        return blogResponses;
    }

    @Override
    public Blog findById(Integer id) {
        return blogRepo.findById(id).get();
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        Page<Blog>blogPage=blogRepo.findByNameContaining(name,pageable);
        Map<String,Object>result= Utility.returnResponse(blogPage);
        return result;

    }

    @Override
    public Blog mapRequestToPoJo(BlogRequest rq) {
        Blog blog = new Blog();
        blog.setName(rq.getName());
        blog.setContent(rq.getContent());
        blog.setCreatDate(LocalDate.now());
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
