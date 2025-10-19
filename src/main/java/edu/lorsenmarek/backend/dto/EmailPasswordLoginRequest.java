package edu.lorsenmarek.backend.dto;

/**
 * DTO Request to login with an email and a password
 * @param email the email
 * @param password the password
 */
public record EmailPasswordLoginRequest(
        String email,
        String password) {}
