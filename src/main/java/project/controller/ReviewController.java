package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.ReviewService;
import project.model.dto.request.ReviewRequest;
import project.model.dto.response.ReviewResponse;
import project.model.shopMess.Message;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/review")
@AllArgsConstructor
public class ReviewController {
    private ReviewService reviewService;

    @PostMapping("add_new_review")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> addNewReview(@RequestBody ReviewRequest reviewRequest){
        try {
            ReviewResponse response = reviewService.addNewReview(reviewRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("/{reviewId}")
    public ReviewResponse getReviewResponseById(@PathVariable int reviewId){
        return reviewService.getReviewResponseById(reviewId);
    }

}
