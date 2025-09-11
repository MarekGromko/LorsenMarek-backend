package edu.lorsenmarek.backend.utility;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatusCode;

import java.io.IOException;
import java.io.PrintWriter;

public class ResponseWrapper {
    private final HttpServletResponse response;
    private PrintWriter writer;
    public ResponseWrapper(HttpServletResponse response) {
        this.response = response;
    }

    public void initWriter() throws IOException {
        this.writer = response.getWriter();
    }
    public ResponseWrapper status(int code) {
        response.setStatus(code);
        return this;
    }
    public ResponseWrapper status(HttpStatusCode statusCode) {
        return status(statusCode.value());
    }
    public ResponseWrapper json(JsonNode node) {
        var body = node.toString();
        response.setContentType("text/json");
        response.setContentLength(body.length());
        writer.write(body);
        return this;
    }
    public ResponseWrapper text(String body) {
        response.setContentType("text/plain");
        response.setContentLength(body.length());
        writer.write(body);
        return this;
    }
    public void flush() {
        writer.flush();
    }
}
