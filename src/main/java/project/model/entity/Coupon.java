package project.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Coupon extends BaseEntity{
    private String couponCode;
    @OneToOne(targetEntity = Users.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "userId")
    private Users users;
    private int couponType;
    private float couponValue;
    private LocalDate startDate;
    private LocalDate endDate;
}
