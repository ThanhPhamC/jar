package project.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "catalog")
@Entity
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int catalogId;
    private String catalogName;
    private boolean catalogStatus;
    @OneToMany (mappedBy = "catalog")
    @JsonIgnore
    private List<Product> productList= new ArrayList<>();
}
