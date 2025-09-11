package edu.lorsenmarek.backend.model;
import com.fasterxml.jackson.databind.JsonNode;
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
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    public static Person fromJson(JsonNode node) {
        if(node == null || node.isObject()) return null;
        return Person.builder()
                .id(node.has("id") ? node.get("id").asInt() : null)
                .firstName(node.has("firstName") ? node.get("firstName").asText() : null)
                .lastName(node.has("lastName") ? node.get("lastName").asText() : null)
                .email(node.has("email") ? node.get("email").asText() : null)
                .gender(node.has("gender") ? node.get("gender").asText() : null)
                .build();
    }
}