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

/**
 *  Controller responsible for authentication and user registration endpoints.
 *  <p>
 *      This class handles user login, signup (registration),
 *      and exception handling related to authentication errors.
 *  </p>
 *
 * @author Lorsen Lamour
 * @author Marek Gromko
 * @version 1.0
 *
 */
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

    /**
     * Authentication a user using email and password.
     * @param req the login request containing user credentials.
     * @return a {@link ResponseEntity} containing a JWT token if authentication is successful.
     * @throws BadCredentialsException if password is incorrect.
     * @throws LockedException if the user account is locked.
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> emailPasswordLogin(@RequestBody EmailPasswordLoginRequest req){
        var token = new EmailPasswordAuthToken(req.email(), req.password());
        authenticationManager.authenticate(token);
        return ResponseEntity.ok(new JwtResponse(jwtUtil.generateToken(req.email())));
    }

    /**
     * Registers a new user in the system.
     * @param req the signup request containing user information (email,name,title,password).
     * @return a {@link ResponseEntity} with no content if registration is successful,
     * or a conflict status if the email is already in use.
     */

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

    /**
     * Handles locked account exception.
     * @return a {@link ResponseEntity} containing an error response with a forbidden status.
     */
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> lockedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(
                "UserLocked",
                "User is locked"
        ));
    }

    /**
     * Handles authentication failure due to incorrect password.
     * @return a {@link ResponseEntity} with a forbidden status and an error message.
     */

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentialsException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(
                "BadPassword",
                "Passwords do not match"
        ));
    }

    /**
     * Handles the case where authentication credentials are missing or invalid.
     * @return a {@link ResponseEntity} with a not found status and an error message.
     */
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ErrorResponse> credentialsNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(
                "UserNotFound",
                "Email matches no user"
        ));
    }
}
