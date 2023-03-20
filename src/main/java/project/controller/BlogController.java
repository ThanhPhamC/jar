package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.BlogService;
import project.model.dto.response.BlogResponse;
import project.model.entity.Blog;
import project.model.shopMess.Message;

import java.util.List;

@RestController
@Configuration("http://localhost:8080")
@RequestMapping("/api/v1/blog")
@AllArgsConstructor
public class BlogController {
    private BlogService blogService;
    @GetMapping("/top_new_blog")
    public ResponseEntity<?> topNewBlog(){
        try {
            List<BlogResponse> responses=blogService.getTopNew();
            if (responses.isEmpty()){
                return new ResponseEntity<>(Message.ERROR_NULL, HttpStatus.BAD_REQUEST);
            }else {
                return new ResponseEntity<>(responses,HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(Message.ERROR_400, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("byId/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") int blogId){
        try {
            Blog blog = blogService.findById(blogId);
            return new ResponseEntity<>(blog, HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.accepted().body(Message.ERROR_400);
        }
    }

}
