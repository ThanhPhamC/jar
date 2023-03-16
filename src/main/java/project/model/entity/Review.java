package project.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "review")
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;
    private int starPoint;
    private String commentContent;
    private boolean reviewStatus;
    @ManyToOne (fetch =  FetchType.EAGER)
    @JoinColumn(name = "userId")
    private Users users;
    @ManyToOne (fetch =  FetchType.EAGER)
    @JoinColumn(name = "productId")
    private Product product;
}
