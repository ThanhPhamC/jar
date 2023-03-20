package project.bussiness.service;

import org.springframework.http.ResponseEntity;
import project.model.dto.request.LogInRequest;
import project.model.dto.request.UserRequest;
import project.model.dto.response.UserResponse;
import project.model.entity.Users;

public interface UserService extends RootService<Users,Integer, UserRequest, UserResponse> {
    Users findByEmail(String email);
    ResponseEntity<?> register(UserRequest userRequest);
    Users saveUpdate(Users users);
    ResponseEntity<?> logIn (LogInRequest logInRequest);
}
