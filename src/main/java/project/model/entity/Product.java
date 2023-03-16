package project.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    private String productName;
    private String productDescriptions;
    private String title;
    private int discount;
    private int productQuantity;
    private String productImg;
    private float importPrice;
    private float exportPrice;
    private boolean productStatus;
    private LocalDate creatDate;
    @ManyToOne (fetch =  FetchType.EAGER)
    @JoinColumn(name = "catalogId")
    private Catalog catalog;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brandId")
    private Brand brand;
    @OneToMany (mappedBy = "product")
    @JsonIgnore
    private List<Review> reviewList = new ArrayList<>();
    @OneToMany (mappedBy = "product")
    private List<SubImg> subImgList= new ArrayList<>();
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<Wish> wishList= new ArrayList<>();
    @OneToMany (mappedBy = "product")
    @JsonIgnore
    private List<CartDetail > cartDetailList = new ArrayList<>();

}
