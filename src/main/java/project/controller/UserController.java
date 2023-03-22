package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.UserService;
import project.model.dto.request.LogInRequest;
import project.model.dto.request.UserRequest;
import project.model.shopMess.Message;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest) {
        try {
            return userService.register(userRequest);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @PostMapping("logIn")
    public ResponseEntity<?> logIn (@RequestBody LogInRequest logInRequest) {
        try {
            return userService.logIn(logInRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @PutMapping("/{userId}")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> blockedUser(@PathVariable int userId, @RequestParam int blockedDays){
        try {
            return userService.blockedUser(userId, blockedDays);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @PutMapping("/unblockUser/{userId}")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> unBlockedUser(@PathVariable int userId){
        try {
            return userService.unBlockedUser(userId);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
}
