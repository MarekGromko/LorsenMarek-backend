package edu.lorsenmarek.backend.controller;

import edu.lorsenmarek.backend.dto.EmailPasswordLoginRequest;
import edu.lorsenmarek.backend.dto.EmailPasswordSigninRequest;
import edu.lorsenmarek.backend.dto.ErrorResponse;
import edu.lorsenmarek.backend.dto.JwtResponse;
import edu.lorsenmarek.backend.model.User;
import edu.lorsenmarek.backend.repository.UserRepository;
import edu.lorsenmarek.backend.security.token.EmailPasswordAuthToken;
import edu.lorsenmarek.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepo;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    PasswordEncoder pwdEncoder;
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> emailPasswordLogin(@RequestBody EmailPasswordLoginRequest req){
        var token = new EmailPasswordAuthToken(req.email(), req.password());
        authenticationManager.authenticate(token);
        return ResponseEntity.ok(new JwtResponse(jwtUtil.generateToken(req.email())));
    }
    @PostMapping("/signin")
    public ResponseEntity<?> emailPasswordSignin(@RequestBody EmailPasswordSigninRequest req) {
        var result = userRepo.findByEmail(req.email());
        if(result.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("EmailAlreadyInUse", "Email is already in Use"));
        }
        User newUser = User.builder()
                .email(req.email())
                .title(req.title())
                .firstName(req.firstName())
                .lastName(req.lastName())
                .pwdAttempts(0)
                .pwdLastAttemptedAt(Instant.now())
                .pwdDigest(pwdEncoder.encode(req.password()))
                .build();
        userRepo.save(newUser);
        return ResponseEntity.noContent().build();
    }
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> lockedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(
                "UserLocked",
                "User is locked"
        ));
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentialsException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(
                "BadPassword",
                "Passwords do not match"
        ));
    }
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ErrorResponse> credentialsNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(
                "UserNotFound",
                "Email matches no user"
        ));
    }
}
