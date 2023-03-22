package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.CommentService;
import project.model.dto.request.CommentRequest;
import project.model.dto.response.CommentResponse;
import project.model.shopMess.Message;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/comment")
@AllArgsConstructor
public class CommentController {
    private CommentService commentService;
    @PostMapping
    public ResponseEntity<?> creatNewCatalog(@RequestBody CommentRequest request){
        try {
            CommentResponse result= commentService.saveOrUpdate(request);
            return  new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(Message.ERROR_400,HttpStatus.BAD_REQUEST);
        }

    }
}
