package edu.lorsenmarek.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * Base error response DTO
 * <p><br/>
 *     All error responses must have a <b><code>code</code></b> field, which is an enum that allows the client to know
 *     the general reason for the error
 * </p>
 * <p>
 *     Error responses may have a <b><code>message</code></b> field, which is a simple display message to describe the
 *     error
 * </p>
 * <p>
 *     Error responses may have a <b><code>details</code></b> field, with custom information & details about the error
 * </p>
 * <p><br/>
 *     <b>Example:</b>
 *     <pre>{@code
 *     {
 *         "code": "SomeError",
 *         "message": "Some error happened!!",
 *         "details": {
 *             "someDetails": 123
 *         }
 *     }
 *     }</pre>
 * </p>
 * @author Marek Gromko
 */
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
