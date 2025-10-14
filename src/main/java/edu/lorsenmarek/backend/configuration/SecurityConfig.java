package edu.lorsenmarek.backend.configuration;

import edu.lorsenmarek.backend.security.EmailPasswordAuthenticationProvider;
import edu.lorsenmarek.backend.security.JwtHttpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class responsible for defining Spring Security setting for the application.
 * <p> This class handles:</p>
 * <ul>
 *     <li>Disabling CSRF and CORS (for stateless API)</li>
 *     <li>Configuring JWT authentication filter</li>
 *     <li>Setting stateless session management</li>
 *     <li>Allowing public access for certain endpoints</li>
 * </ul>
 *  All other endpoints require authentication with a valid JWT token
 *
 * @author Marek Gromko
 * @version 1.0
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    JwtHttpFilter jwtHttpFilter;

    /**
     * Defines the password encoder bean used across the authentication process.
     * @return a {@link BCryptPasswordEncoder} INSTANCE for securely hashing password.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines the custom authentication manager that uses the application
     * {@link EmailPasswordAuthenticationProvider}.
     * @param emailPasswordAuthenticationProvider the custom provider handling email & password authentication
     * @return a configure {@link AuthenticationManager} instance.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            EmailPasswordAuthenticationProvider emailPasswordAuthenticationProvider
    ) {
        return new ProviderManager(
                emailPasswordAuthenticationProvider
        );
    }

    /**
     * Defines the core security filter chain for all HTTP request.
     * @param httpSecurity the {@link HttpSecurity} configuration builder.
     * @return a configured {@link SecurityFilterChain} instance that defines the app security rules
     * @throws Exception if there is an error building the security configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws  Exception{
        httpSecurity.csrf(csrf ->csrf.disable())
                .cors(cors->cors.disable())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/auth/**", "/public/**").permitAll()
                                .anyRequest().authenticated()
                );
        httpSecurity.addFilterBefore(jwtHttpFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
