package App.controller;

import App.model.User;
import App.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Эндпоинт для регистрации пользователя
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User userRequest) {
        System.out.println(userRequest.getName() + " " + userRequest.getPassword());
        return userService.addUser(userRequest.getName(), userRequest.getPassword(),passwordEncoder);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User userRequest) {
        return userService.authenticateUser(userRequest.getName(),userRequest.getPassword(),passwordEncoder);
    }
}
