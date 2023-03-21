package project.bussiness.service;

import project.model.dto.request.ReviewRequest;
import project.model.dto.response.ReviewResponse;
import project.model.entity.Review;

public interface ReviewService extends RootService<Review,Integer, ReviewRequest, ReviewResponse> {
    ReviewResponse addNewReview(ReviewRequest reviewRequest);
    ReviewResponse getReviewResponseById(int reviewId);
}
