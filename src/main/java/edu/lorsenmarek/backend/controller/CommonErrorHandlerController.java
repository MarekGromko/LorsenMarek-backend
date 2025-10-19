package edu.lorsenmarek.backend.controller;

import edu.lorsenmarek.backend.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

/**
 * Global Controller to customize the behaviors of common pipeline errors
 * @author Marek Gromko
 */
@ControllerAdvice
public class CommonErrorHandlerController {
    private CommonErrorHandlerController() {}
    /**
     * Handler for an empty request body when one is required
     *
     * @param ex handling exception <code>HttpMessageNotReadableException</code>
     * @return an <code>ErrorResponse</code> with code <code>MissingRequiredBody</code>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> bodyNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body( new ErrorResponse(
                "MissingRequiredBody"
        ));
    }
}
