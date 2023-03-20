package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.bussiness.service.CouponService;
import project.model.dto.request.CouponRequest;
import project.model.dto.response.CouponResponse;
import project.model.entity.Coupon;
import project.model.shopMess.Constants;
import project.repository.CouponRepository;
import project.security_jwt.CustomUserDetails;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CouponImpl implements CouponService {
    private CouponRepository couponRepo;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        return null;
    }

    @Override
    public CouponResponse saveOrUpdate(CouponRequest couponRequest) {
        return null;
    }

    @Override
    public CouponResponse update(Integer id, CouponRequest couponRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        return null;
    }

    @Override
    public List<Coupon> findAll() {
        return null;
    }
    @Override
    public List<CouponResponse> getAllForClient() {
        CustomUserDetails customUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<CouponResponse> responseList = couponRepo.findByStatusAndUsers_UserId(Constants.ONLINE, customUser.getUserId())
                .stream()
                .map(this::mapPoJoToResponse)
                .filter(response -> response.getStatus()==Constants.ONLINE)
                .collect(Collectors.toList());
        return responseList;
    }

    @Override
    public Coupon findById(Integer id) {
        return null;
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public Coupon mapRequestToPoJo(CouponRequest couponRequest) {
        return null;
    }

    @Override
    public CouponResponse mapPoJoToResponse(Coupon coupon) {
        CouponResponse response = new CouponResponse();
        response.setId(coupon.getId());
        response.setName(coupon.getName());
        response.setCouponValue(coupon.getCouponValue());
        Duration duration = Duration.between(LocalDateTime.now(), coupon.getEndDate());
        long seconds = duration.getSeconds();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM dd, yyyy HH:mm");
        String str = String.format("%d ngày %d giờ %d phút",
                seconds / 86400,
                (seconds % 86400) / 3600,
                ((seconds % 86400) % 3600) / 60);
        response.setExpiration(str);
        if (LocalDateTime.now().compareTo(coupon.getStartDate())<0||LocalDateTime.now().compareTo(coupon.getEndDate())>0){
            coupon.setStatus(Constants.OFFLINE);
            couponRepo.save(coupon);
        };
        response.setStatus(coupon.getStatus());
        return response;
    }
}
