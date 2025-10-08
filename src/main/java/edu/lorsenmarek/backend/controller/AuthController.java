package edu.lorsenmarek.backend.controller;

import edu.lorsenmarek.backend.model.User;
import edu.lorsenmarek.backend.repository.UserRepository;
import edu.lorsenmarek.backend.security.JwtUtil;
import edu.lorsenmarek.backend.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
 @Autowired
    AuthenticationManager authenticationManager;
 @Autowired UserRepository userRepository;
 @Autowired PasswordEncoder encoder;
 @Autowired JwtUtil jwtUtil;

 @PostMapping("/login")
    public String authenticateUser(@RequestBody User user){
     Authentication authentication = authenticationManager.authenticate(
             new UsernamePasswordAuthenticationToken(
                     user.getEmail(),
                     user.getPwdDigest()
             )
     );
     UserDetails userDetails = (UserDetails) authentication.getPrincipal();
     return jwtUtil.generateToken(userDetails.getUsername());
 }
    @PostMapping("/signup")
    public String registerUser(@RequestBody User user) {
        if (userRepository.exitByemail(user.getEmail())) {
            return "Error: Username is already taken!";
        }
        User newUser = User.builder()
                .email(user.getEmail())
                .pwdDigest(encoder.encode(user.getPwdDigest()))
                .build();
        userRepository.save(newUser);
        return "User registered successfully!";
    }

}
