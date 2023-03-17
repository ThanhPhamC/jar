package project.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Table(name = "users")
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String userName;
    private String passwords;
    private String firstName;
    private  String lastName;
    private  String email;
    private String phone;
    private String address;
    private String country;
    private String city;
    private String state;
    private LocalDateTime birtDate;
    private String avatar;
    private int ranking;
    private boolean userStatus;
    @ManyToMany(fetch =  FetchType.EAGER)
    @JoinTable( name = "user_role", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Roles> listRoles = new HashSet<>();
    @OneToMany (mappedBy = "users")
    @JsonIgnore
    private List<Cart> cartList = new ArrayList<>();
    @OneToMany(mappedBy = "users")
    @JsonIgnore
    private List<Review> reviewList= new ArrayList<>();

}
