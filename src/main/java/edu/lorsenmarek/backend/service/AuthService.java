package edu.lorsenmarek.backend.service;

import edu.lorsenmarek.backend.model.User;
import edu.lorsenmarek.backend.repository.UserRepository;
import edu.lorsenmarek.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

  /*  public String login(String email, String password){
        User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(password,user.getPwdDigest())){
            throw new RuntimeException("password incorect");
        }
        return  jwtUtil.
  }  */
}
