package edu.lorsenmarek.backend.security;

import java.io.IOException;
import java.util.Collections;

import edu.lorsenmarek.backend.repository.UserRepository;
import edu.lorsenmarek.backend.security.token.DetailedAuthToken;
import edu.lorsenmarek.backend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT authentication filter that intercepts incoming HTTP request
 * and validates the JWT token if present
 * @author Lorsen Lamour
 * @version 1.0
 */
@Component
public class JwtHttpFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRepository userRepo;

    /**
     * Filters each HTTP request to perform JWT-based authentication.
     *
     * <p>If a valid JWT is found in the authorization header this method retrieves the corresponding user and sets a {@link DetailedAuthToken}.</p>
     *
     * @param request the http request
     * @param response the http response
     * @param filterChain the filter chain
     * @throws ServletException in case of servlet errors
     * @throws IOException case od IO errors
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = extractJwt(request);
            if (jwt != null && jwtUtil.validateJwtToken(jwt)) {
                String email = jwtUtil.getSubjectFromToken(jwt);
                userRepo.findByEmail(email).ifPresent(user -> {
                    var token = new DetailedAuthToken(user, Collections.emptyList());
                    token.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication(token);
                });
            }
        } finally {
            filterChain.doFilter(request,response);
        }
    }

    /**
     * Extracts the JWT token from the Authorization header of the HTTP request.
     *
     * @param request the http request
     * @return the JWT token if present and starts with "Bearer", otherwise {@code null}
     */
    private String extractJwt(HttpServletRequest request){
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")){
            return headerAuth.substring(7);
        }
        return null;
    }
}