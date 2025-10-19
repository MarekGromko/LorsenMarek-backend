package edu.lorsenmarek.backend.dto;

/**
 * DTO to give a JWT
 * @param jwt the raw JWT
 */
public record JwtResponse (String jwt){}
