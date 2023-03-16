package project.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="brand")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int brandId;
    private String brandName;
    private String brandLogo;
    private boolean brandStatus;
    @OneToMany(mappedBy = "brand")
    @JsonIgnore
    private List<Product> productList= new ArrayList<>();
}
