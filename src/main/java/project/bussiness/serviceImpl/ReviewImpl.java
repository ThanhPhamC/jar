package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.bussiness.service.ReviewService;
import project.model.dto.request.ReviewRequest;
import project.model.dto.response.ReviewResponse;
import project.model.entity.Product;
import project.model.entity.Review;
import project.model.entity.Users;
import project.repository.ProductRepository;
import project.repository.ReviewRepository;
import project.repository.UserRepository;
import project.security_jwt.CustomUserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ReviewImpl implements ReviewService {
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private ReviewRepository reviewRepository;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        return null;
    }

    @Override
    public ReviewResponse saveOrUpdate(ReviewRequest reviewRequest) {
        return null;
    }

    @Override
    public ReviewResponse update(Integer id, ReviewRequest reviewRequest) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public List<Review> findAll() {
        return null;
    }

    @Override
    public List<ReviewResponse> getAllForClient() {
        return null;
    }

    @Override
    public Review findById(Integer id) {
        return null;
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public Review mapRequestToPoJo(ReviewRequest reviewRequest) {
        return null;
    }

    @Override
    public ReviewResponse mapPoJoToResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setFirstName(review.getUsers().getFirstName());
        response.setLastName(review.getUsers().getLastName());
        response.setAvatar(review.getUsers().getAvatar());
        response.setComment(review.getCommentContent());
        response.setStarPoint(response.getStarPoint());
        response.setStatus(review.getStatus());
        LocalDate now = LocalDate.now();
        Period period = Period.between(review.getCreateDate(), now);
        int days = period.getDays();
        int months = period.getMonths();
        int years = period.getYears();
        if (days!=0 && months==0 && years==0) {
            response.setDaysAgo(days + " days ago");
        } else if (months !=0 && years ==0) {
            response.setDaysAgo(months + " months ago");
        } else if(years!=0){
            response.setDaysAgo(years + " years ago");
        }
        return response;
    }

    @Override
    public ReviewResponse addNewReview(ReviewRequest reviewRequest) {
        CustomUserDetails userIsLoggingIn = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userIsLoggingIn.getUsername());
        Product product = productRepository.findById(reviewRequest.getProductId()).get();

        Review review = new Review();
        review.setUsers(users);
        review.setProduct(product);
        review.setCommentContent(reviewRequest.getCommentContent());
        review.setStarPoint(reviewRequest.getStarPoint());
        review.setStatus(0);
        LocalDate now = LocalDate.now();
        review.setCreateDate(now);
        reviewRepository.save(review);

        return mapPoJoToResponse(review);
    }

    @Override
    public ReviewResponse getReviewResponseById(int reviewId) {
        return mapPoJoToResponse(reviewRepository.findById(reviewId).get());
    }
}
