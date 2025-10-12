package edu.lorsenmarek.backend.controller;

import edu.lorsenmarek.backend.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonErrorHandlerController {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> bodyNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body( new ErrorResponse(
                "MissingRequiredBody"
        ));
    }
}
