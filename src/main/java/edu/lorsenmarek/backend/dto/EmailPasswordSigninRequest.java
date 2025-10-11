package edu.lorsenmarek.backend.dto;

public record EmailPasswordSigninRequest(
        String email,
        String password,
        String title,
        String firstName,
        String lastName) {
}
