package project.bussiness.serviceImpl;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import project.bussiness.service.CartService;
import project.bussiness.service.CouponService;
import project.bussiness.service.UserService;
import project.model.dto.request.LogInRequest;
import project.model.dto.request.UserRequest;
import project.model.dto.response.*;
import project.model.entity.*;
import project.model.regex.RegexValidate;
import project.model.shopMess.Message;
import project.repository.CartRepository;
import project.repository.RoleRepository;
import project.repository.TokenLogInReposirory;
import project.repository.UserRepository;
import project.security_jwt.CustomUserDetails;
import project.security_jwt.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder encoder;
    private RoleRepository roleRepository;
    private CartRepository cartRepository;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private CartService cartService;
    private CouponService couponService;
    private TokenLogInReposirory tokenLogInReposirory;

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
    public ResponseEntity<?> delete(Integer id) {
        return null;
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

    @Override
    public ResponseEntity<?> blockedUser(int userId, int blockedDays) {
        Users users = userRepository.findById(userId).get();
        LocalDate blockDate = LocalDate.now().plusDays(blockedDays);
        users.setBlockedDate(blockDate);
        users.setUserStatus(false);
        userRepository.save(users);
        return ResponseEntity.ok().body("Blocked in " + blockedDays + " days");
    }

    @Override
    public ResponseEntity<?> unBlockedUser(int userId) {
        Users users = userRepository.findById(userId).get();
        users.setUserStatus(true);
        users.setBlockedDate(null);
        userRepository.save(users);
        return ResponseEntity.ok().body(Message.UNBLOCK_USER_SUCCESS);
    }

    @Override
    public ResponseEntity<?> logIn(LogInRequest logInRequest) {
        Users users = userRepository.findUsersByUserName(logInRequest.getUserName());
        LocalDate now = LocalDate.now();
        if (users.getBlockedDate() != null && now.isAfter(users.getBlockedDate()) ) {
            users.setUserStatus(true);
            userRepository.save(users);
        }
        if (users.isUserStatus() && !tokenLogInReposirory.existsByUsers_UserId(users.getUserId())) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(logInRequest.getUserName(), logInRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails customUserDetail = (CustomUserDetails) authentication.getPrincipal();
            //Sinh JWT tra ve client
            String jwt = tokenProvider.generateToken(customUserDetail);
            //Lay cac quyen cua user
            List<String> listRoles = customUserDetail.getAuthorities().stream()
                    .map(item -> item.getAuthority()).collect(Collectors.toList());
            Cart cart = customUserDetail.getListCart().get(customUserDetail.getListCart().size() - 1);
            CartResponse cartResponse = cartService.mapPoJoToResponse(cart);
            List<CouponResponse> couponResponses = couponService.getAllForClient();
            JwtResponse response = new JwtResponse(jwt, customUserDetail.getUserId(), customUserDetail.getUsername(), customUserDetail.getFirstName(), customUserDetail.getLastName(),
                    customUserDetail.getEmail(), customUserDetail.getAddress(), customUserDetail.getState(), customUserDetail.getCity(), customUserDetail.getCounty(),
                    customUserDetail.getPhone(), customUserDetail.getAvatar(), customUserDetail.getBirtDate(), customUserDetail.isUserStatus(), customUserDetail.getRanking(),
                    listRoles, cartResponse/*,couponResponses*/);

            TokenLogIn tokenLogIn = new TokenLogIn();
            tokenLogIn.setName(jwt);
            tokenLogIn.setUsers(users);
            tokenLogIn.setStatus(1);
            tokenLogInReposirory.save(tokenLogIn);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body(Message.ERROR_LOCKED_USER);
        }
    }

    @Override
    public ResponseEntity<?> logOut() {
        CustomUserDetails userIsLoggingIn = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userIsLoggingIn.getUsername());
        TokenLogIn tokenLogIn = tokenLogInReposirory.findByUsers_UserId(users.getUserId());
        tokenLogInReposirory.deleteById(tokenLogIn.getId());
        return ResponseEntity.ok().body(Message.LOGOUT_SUCCESS);
    }

}
