package project.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import project.model.entity.Cart;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class JwtResponse {
    private String type = "Bearer";
    private String token;
    private int userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String state;
    private String city;
    private String country;
    private String phone;
    private String avatar;
    private LocalDateTime birtDate;
    private boolean userStatus;
    private int ranking;
    private List<String> listRoles;
    @JsonIgnore
    private Cart cart;

    public JwtResponse(String token, int userId, String userName, String firstName, String lastName, String email, String address, String state, String city, String country, String phone, String avatar, LocalDateTime birtDate, boolean userStatus, int ranking, List<String> listRoles, Cart cart) {
        this.token = token;
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.state = state;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.avatar = avatar;
        this.birtDate = birtDate;
        this.userStatus = userStatus;
        this.ranking = ranking;
        this.listRoles = listRoles;
        this.cart = cart;
    }
}
