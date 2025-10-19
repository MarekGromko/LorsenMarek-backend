package edu.lorsenmarek.backend.dto;

/**
 * A DTO describing a request to create a new account
 *
 * @see edu.lorsenmarek.backend.model.User
 * @see EmailPasswordLoginRequest
 * @see edu.lorsenmarek.backend.security.token.EmailPasswordAuthToken
 * @param email the user email
 * @param password the user password
 * @param title the user title (Mrs, Mme)
 * @param firstName the user first name
 * @param lastName the user last name
 */
public record SigninRequest(
        String email,
        String password,
        String title,
        String firstName,
        String lastName) {
}
