package project.bussiness.service;

import org.springframework.stereotype.Repository;
import project.model.dto.request.UserRequest;
import project.model.dto.response.UserResponse;
import project.model.entity.Users;
@Repository
public interface UserService extends RootService<Users,Integer, UserRequest, UserResponse> {
}
