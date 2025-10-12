package edu.lorsenmarek.backend.security;

import edu.lorsenmarek.backend.model.User;
import edu.lorsenmarek.backend.repository.UserRepository;
import edu.lorsenmarek.backend.security.token.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;

@Component
public class EmailPasswordAuthenticationProvider implements AuthenticationProvider {
    final private Integer PWD_ATTEMPTS_MAX;
    final private Duration PWD_ATTEMPTS_LOCKOUT;
    @Autowired
    PasswordEncoder pwdEncoder;
    @Autowired
    UserRepository userRepo;
    public EmailPasswordAuthenticationProvider(
            @Value("${auth.providers.emailpassword.attempts.max:0}") String pwdAttemptsMax,
            @Value("${auth.providers.emailpassword.attempts.lockout:PT10M}") String pwdAttemptsLockout
    ) {
        PWD_ATTEMPTS_MAX = Integer.parseInt(pwdAttemptsMax);
        PWD_ATTEMPTS_LOCKOUT = Duration.parse(pwdAttemptsLockout);
    }
    private void tryUserLockout(User user) {
        if(PWD_ATTEMPTS_MAX > 0 &&
                user.getPwdAttempts() >= PWD_ATTEMPTS_MAX &&
                user.getPwdLastAttemptedAt().isBefore(Instant.now().minus(PWD_ATTEMPTS_LOCKOUT))
        ) {
            throw new LockedException("User is locked out from attempting to authenticate");
        }
    }
    private void tryMatchingPassword(User user, String plainPassword) {
        if(pwdEncoder.matches(plainPassword, user.getPwdDigest())) {
            user.setPwdAttempts(0);
            user.setPwdLastAttemptedAt(Instant.now());
            userRepo.save(user);
        } else {
            user.setPwdAttempts(user.getPwdAttempts()+1);
            user.setPwdLastAttemptedAt(Instant.now());
            userRepo.save(user);
            throw new BadCredentialsException("Password does not match");
        }
    }
    private DetailedAuthToken authEmailPassword(final EmailPasswordAuthToken token) {
        var result = userRepo.findByEmail(token.getName());
        if(result.isEmpty())
            throw new AuthenticationCredentialsNotFoundException("User with ID [%s] not found");
        User user = result.get();
        tryUserLockout(user);
        tryMatchingPassword(user, token.getCredentials());
        return new DetailedAuthToken(user, Collections.emptyList());
    }
    @Override
    public DetailedAuthToken authenticate(final Authentication authToken) throws AuthenticationException {
        if(authToken instanceof EmailPasswordAuthToken)
            return authEmailPassword((EmailPasswordAuthToken) authToken);
        throw new AuthenticationCredentialsNotFoundException("Unknown authentication method");
    }
    @Override
    public boolean supports(Class<?> authClass) {
        return authClass.equals(EmailPasswordAuthToken.class);
    }
}
