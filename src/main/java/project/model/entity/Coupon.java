package project.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
public class Coupon extends BaseEntity{
    private String couponCode;
    @OneToOne(targetEntity = Users.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "userId")
    private Users users;
    private int couponType;
    private float couponValue;
    private LocalDateTime startDate;
    private LocalDate endDate;
}
