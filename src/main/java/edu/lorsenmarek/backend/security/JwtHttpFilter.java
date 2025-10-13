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

@Component
public class JwtHttpFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRepository userRepo;
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
    private String extractJwt(HttpServletRequest request){
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")){
            return headerAuth.substring(7);
        }
        return null;
    }
}