package edu.lorsenmarek.backend.model;
import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String pwdDigest;
    private String title;
}