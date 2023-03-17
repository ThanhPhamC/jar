package project.security_jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.model.entity.Cart;
import project.model.entity.Users;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {
    private int userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private String passwords;
    private String address;
    private String state;
    private String city;
    private String county;
    private String phone;
    private String avatar;
    private LocalDateTime birtDate;
    private boolean statusUser;
    private int ranks;
    private List<Cart> cart;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public static CustomUserDetails mapUserToUserDetail(Users users) {
        List<GrantedAuthority> listAuthority = users.getListRoles().stream()
                .map(roles -> new SimpleGrantedAuthority(roles.getRoleName().name()))
                .collect(Collectors.toList());
        return new CustomUserDetails(users.getUserId(), users.getUserName(), users.getFirstName(), users.getLastName(), users.getEmail(), users.getPasswords(), users.getAddress(),
                users.getState(), users.getCity(), users.getCountry(), users.getPhone(), users.getAvatar(),users.getBirtDate(),users.isUserStatus(),users.getRanking(),users.getCartList(),listAuthority);
    }
    @Override
    public String getPassword() {
        return this.passwords;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }
}
