package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.bussiness.service.ReviewService;
import project.model.dto.request.ReviewRequest;
import project.model.dto.response.ReviewResponse;
import project.model.entity.*;
import project.repository.*;
import project.security_jwt.CustomUserDetails;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ReviewImpl implements ReviewService {
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private ReviewRepository reviewRepository;
    private CartRepository cartRepository;
    private CartDetailRepository cartDetailRepository;

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
    public ResponseEntity<?> delete(Integer id) {
        return null;
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
        LocalDateTime now = LocalDateTime.now();
        long years = ChronoUnit.YEARS.between(review.getCreateDate(), now);
        long months = ChronoUnit.MONTHS.between(review.getCreateDate(), now);
        long weeks = ChronoUnit.WEEKS.between(review.getCreateDate(), now);
        long days = ChronoUnit.DAYS.between(review.getCreateDate(), now);
        long hours = ChronoUnit.HOURS.between(review.getCreateDate(), now);
        long minutes = ChronoUnit.MINUTES.between(review.getCreateDate(), now);
        long seconds = ChronoUnit.SECONDS.between(review.getCreateDate(), now);
        if (seconds==0){
            response.setDaysAgo( "just now");
        }
        if (seconds!=0 && minutes==0 && hours ==0&& days ==0 && weeks==0 && months == 0&& years==0) {
            response.setDaysAgo(seconds + " seconds ago");
        }
        if (minutes!=0 && hours ==0&& days ==0 && weeks==0 && months == 0&& years==0){
            response.setDaysAgo(minutes + " minutes ago");
        }
        if (hours!=0&& days ==0 && weeks==0 && months == 0&& years==0){
            response.setDaysAgo(hours + " hours ago");
        }
        if (days != 0 && weeks==0 && months == 0 && years == 0) {
            response.setDaysAgo(days + " days ago");
        }
        if (weeks!=0 && months == 0&& years==0){
            response.setDaysAgo(weeks + " weeks ago");
        }
        if (months != 0 && years == 0) {
            response.setDaysAgo(months + " months ago");
        }
        if (years != 0) {
            response.setDaysAgo(years + " years ago");
        }
        return response;
    }

    @Override
    public ReviewResponse addNewReview(ReviewRequest reviewRequest) {
        CustomUserDetails userIsLoggingIn = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userIsLoggingIn.getUsername());
        Product product = productRepository.findById(reviewRequest.getProductId()).get();
        List<Cart> cartList = cartRepository.findByUsers_UserIdAndStatus(users.getUserId(), 4);
        List<CartDetail> cartDetailList = cartDetailRepository.findByCartIn(cartList);
        Review review = new Review();
        for (CartDetail cd : cartDetailList) {
            if (cd.getProduct().getId() == reviewRequest.getProductId()) {
                review.setUsers(users);
                review.setProduct(product);
                review.setCommentContent(reviewRequest.getCommentContent());
                review.setStarPoint(reviewRequest.getStarPoint());
                review.setStatus(0);
                LocalDateTime now = LocalDateTime.now();
                review.setCreateDate(now);
                reviewRepository.save(review);
            }
        }
        return mapPoJoToResponse(review);
    }

    @Override
    public ReviewResponse getReviewResponseById(int reviewId) {
        return mapPoJoToResponse(reviewRepository.findById(reviewId).get());
    }
}
