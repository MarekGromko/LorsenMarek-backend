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


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    JwtHttpFilter jwtHttpFilter;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(
            EmailPasswordAuthenticationProvider emailPasswordAuthenticationProvider
    ) {
        return new ProviderManager(
                emailPasswordAuthenticationProvider
        );
    }
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
