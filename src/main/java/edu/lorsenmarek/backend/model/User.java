package edu.lorsenmarek.backend.model;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;

/**
 * A fully described user
 */
@Data
@AllArgsConstructor
@Builder
public class User {
    /** Create a new {@link User} */
    public User() {}
    @Id
    private Long id;
    private String title;
    private String firstName;
    private String lastName;
    private String email;
    private String pwdDigest;
    private Integer pwdAttempts;
    private Instant pwdLastAttemptedAt;
}