package edu.lorsenmarek.backend.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import java.util.Collections;

public class EmailPasswordAuthToken extends AbstractAuthenticationToken {
    final private String email;
    final private String password;
    public EmailPasswordAuthToken(String email, String password) {
        super(Collections.emptyList());
        this.email = email;
        this.password = password;
    }
    @Override
    public String getCredentials() {
        return password;
    }
    @Override
    public String getPrincipal() {
        return email;
    }
}
