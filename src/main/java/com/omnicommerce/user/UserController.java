package com.omnicommerce.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody UserDTO userDTO) {
        User user = userService.save(userDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDTO userDTO) {
        Map<String, String> bearToken = new HashMap<>();
        bearToken.put("bearToken", userService.login(userDTO));
        return new ResponseEntity<>(bearToken, HttpStatus.OK);
    }

    @GetMapping("/test-jwt")
    public String testJwt(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return "you are eligible to pass this wall";
    }
}