package project.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "subImg")
public class SubImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subImgId;
    private String subLink;
    private boolean subImgStatus;
    @ManyToOne (fetch =  FetchType.EAGER)
    @JoinColumn( name = "productId")
    @JsonIgnore
    private Product product;

}
