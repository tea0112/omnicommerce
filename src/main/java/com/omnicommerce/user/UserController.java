package com.omnicommerce.user;

import com.omnicommerce.golobal.exception.LoginException;
import com.omnicommerce.golobal.exception.UserNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody UserDTO userDTO) throws ConstraintViolationException {
        User user = userService.save(userDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDTO userDTO) throws UserNotFoundException, LoginException {
        Map<String, String> BearerToken = new HashMap<>();
        BearerToken.put("BearerToken", userService.login(userDTO));
        return new ResponseEntity<>(BearerToken, HttpStatus.OK);
    }

    @GetMapping("/test-jwt")
    public String testJwt(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return "you are eligible to pass this wall";
    }
}