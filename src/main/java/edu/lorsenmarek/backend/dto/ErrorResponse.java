package edu.lorsenmarek.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String code;
    private String message;
    private Object details;
    public ErrorResponse(String code) {
        this.code = code;
        this.message = null;
        this.details = null;
    }
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.details = null;
    }
    public ErrorResponse(String code, String message, Object details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }
}
