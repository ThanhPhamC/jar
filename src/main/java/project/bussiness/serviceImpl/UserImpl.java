package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.bussiness.service.UserService;
import project.model.dto.request.UserRequest;
import project.model.dto.response.UserResponse;
import project.model.entity.Cart;
import project.model.entity.ERole;
import project.model.entity.Roles;
import project.model.entity.Users;
import project.model.regex.RegexValidate;
import project.model.shopMess.Message;
import project.repository.CartRepository;
import project.repository.RoleRepository;
import project.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder encoder;
    private RoleRepository roleRepository;
    private CartRepository cartRepository;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        return null;
    }

    @Override
    public UserResponse saveOrUpdate(UserRequest userRequest) {
        return null;
    }

    @Override
    public UserResponse update(Integer id, UserRequest userRequest) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public List<Users> findAll() {
        return null;
    }

    @Override
    public List<UserResponse> getAllForClient() {
        return null;
    }

    @Override
    public Users findById(Integer id) {
        return null;
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public Users mapRequestToPoJo(UserRequest userRequest) {
        return null;
    }

    @Override
    public UserResponse mapPoJoToResponse(Users users) {
        return null;
    }

    @Override
    public Users findByEmail(String email) {
        return null;
    }

    @Override
    public ResponseEntity<?> register(UserRequest userRequest) {
        if (userRepository.existsByUserName(userRequest.getUserName())) {
            return ResponseEntity.badRequest().body(Message.ERROR_EXISTED_USERNAME);
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return ResponseEntity.badRequest().body(Message.ERROR_EXISTED_EMAIL);
        }
        if (!RegexValidate.checkRegexEmail(userRequest.getEmail())) {
            return ResponseEntity.badRequest().body(Message.ERROR_INVALID_EMAIL);
        }
        if (!RegexValidate.checkRegexPhone(userRequest.getPhone())) {
            return ResponseEntity.badRequest().body(Message.ERROR_INVALID_PHONE);
        }
        if (!RegexValidate.checkRegexPassword(userRequest.getPassword())) {
            return ResponseEntity.badRequest().body(Message.ERROR_INVALID_PASSWORD);
        }
        Users user = new Users();
        user.setUserName(userRequest.getUserName());
        user.setPassword(encoder.encode(userRequest.getPassword()));
        user.setAvatar(userRequest.getAvatar());
        user.setLastName(userRequest.getLastName());
        user.setFirstName(userRequest.getFirstName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setAddress(userRequest.getAddress());
        user.setState(userRequest.getState());
        user.setCity(userRequest.getCity());
        user.setCountry(userRequest.getCounty());
        user.setBirtDate(userRequest.getBirtDate());
        user.setRanking(0);
        user.setUserStatus(true);
        Set<String> strRoles = userRequest.getListRoles();
        Set<Roles> listRoles = new HashSet<>();
        if (strRoles == null) {
            //User quyen mac dinh
            Roles userRole = (Roles) roleRepository.findByRoleName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException(Message.ERROR_ROLE_NOT_FOUND));
            listRoles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Roles adminRole = null;
                        try {
                            adminRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException(Message.ERROR_ROLE_NOT_FOUND));
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                        listRoles.add(adminRole);
                    case "moderator":
                        Roles modRole = null;
                        try {
                            modRole = roleRepository.findByRoleName(ERole.ROLE_MODERATOR)
                                    .orElseThrow(() -> new RuntimeException(Message.ERROR_ROLE_NOT_FOUND));
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                        listRoles.add(modRole);
                    case "user":
                        Roles userRole = null;
                        try {
                            userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException(Message.ERROR_ROLE_NOT_FOUND));
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                        listRoles.add(userRole);
                }
            });
        }
        user.setListRoles(listRoles);
        Users result = saveUpdate(user);
        Cart cart = new Cart();
        cart.setUsers(result);
        cart.setStatus(0);
        cartRepository.save(cart);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public Users saveUpdate(Users users) {
        return userRepository.save(users);
    }
}