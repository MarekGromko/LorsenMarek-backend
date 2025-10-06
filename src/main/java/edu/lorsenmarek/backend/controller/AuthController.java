package edu.lorsenmarek.backend.controller;

import edu.lorsenmarek.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
 @Autowired private AuthService authService;

 @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String,String> request){
     String email = request.get("email");
     String password = request.get("password");
     String token = authService.login(email,password);
     return  Map.of("token",token);
 }
}
