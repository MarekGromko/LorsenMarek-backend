package edu.lorsenmarek.backend.dto;

public record SigninRequest(
        String email,
        String password,
        String title,
        String firstName,
        String lastName) {
}
