package edu.lorsenmarek.backend.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {
    private Integer id;
    private String fullName;
    private String lastName;
    private String email;
    private String gender;
}