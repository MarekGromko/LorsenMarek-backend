package edu.lorsenmarek.backend.config;

import edu.lorsenmarek.backend.model.User;
import edu.lorsenmarek.backend.security.token.DetailedAuthToken;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;
import java.util.Optional;

/**
 * This is a configuration class that allow to populate the SecurityContext with an Authentication
 */
@TestConfiguration
public class MockSecurityConfig {
    final public static User defaultUserStub = User.builder()
            .id(1L)
            .email("test@user.com")
            .build();
    public static class AuthMiddleware {
        public Optional<DetailedAuthToken> getAuthToken() {
            var token = new DetailedAuthToken(defaultUserStub, Collections.emptyList());
            token.setAuthenticated(true);
            return Optional.of(token);
        }
    }
    @Bean
    public AuthMiddleware defaultAuthFilter() {
        return new AuthMiddleware();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthMiddleware authFilter) throws Exception{
        http.csrf(csrf ->csrf.disable())
                .cors(cors->cors.disable())
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll()
                );
        http.addFilterBefore((req, res, fc) -> {
            authFilter.getAuthToken().ifPresent(authToken->{
                SecurityContextHolder.getContext().setAuthentication(authToken);
            });
            fc.doFilter(req, res);
        }, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
