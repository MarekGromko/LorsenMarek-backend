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

/**
 * Authentication provider that handles user authentication form email and password.
 *  @author Marek Gromko
 * @version 1.0
 *
 * <p>This class implements {@link AuthenticationProvider} and performs the following: </p>
 * <ul>
 *     <li>Checks if the user exists in the system by email </li>
 *     <li> Verifies the password against the hashed digest </li>
 *     <li> Tracks failed login attempts and enforces lockout policies </li>
 *     <li> Resets the attempts counter  </li>
 * </ul>
 */
@Component
public class EmailPasswordAuthenticationProvider implements AuthenticationProvider {
    final private Integer PWD_ATTEMPTS_MAX;
    final private Duration PWD_ATTEMPTS_LOCKOUT;
    @Autowired
    PasswordEncoder pwdEncoder;
    @Autowired
    UserRepository userRepo;

    /**
     * Constructs the authentication provider with configuration values.
     *
     * @param pwdAttemptsMax maximum password attempts before lockout from app properties
     * @param pwdAttemptsLockout duration of lockout period from app properties
     */
    public EmailPasswordAuthenticationProvider(
            @Value("${auth.providers.emailpassword.attempts.max:0}") String pwdAttemptsMax,
            @Value("${auth.providers.emailpassword.attempts.lockout:PT10M}") String pwdAttemptsLockout
    ) {
        PWD_ATTEMPTS_MAX = Integer.parseInt(pwdAttemptsMax);
        PWD_ATTEMPTS_LOCKOUT = Duration.parse(pwdAttemptsLockout);
    }

    /**
     * Checks the user is currently locked out due to many failed login attempts
     * @param user the user to check
     * @throws LockedException if the user is locked out
     */
    private void tryUserLockout(User user) {
        if(PWD_ATTEMPTS_MAX > 0 &&
                user.getPwdAttempts() >= PWD_ATTEMPTS_MAX &&
                user.getPwdLastAttemptedAt().isBefore(Instant.now().minus(PWD_ATTEMPTS_LOCKOUT))
        ) {
            throw new LockedException("User is locked out from attempting to authenticate");
        }
    }

    /**
     * Verifies that the provider password matches the stored passW digest.
     * @param user the user to authenticate
     * @param plainPassword the plain-text password does not match
     * @throws BadCredentialsException if the passW does not match
     */
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

    /**
     * Performs email , password authenticate for given token
     * @param token the {@link EmailPasswordAuthToken} containing user credentials
     * @return a {@link  DetailedAuthToken} if authentication succeeds
     */
    private DetailedAuthToken authEmailPassword(final EmailPasswordAuthToken token) {
        var result = userRepo.findByEmail(token.getName());
        if(result.isEmpty())
            throw new AuthenticationCredentialsNotFoundException("User with ID [%s] not found");
        User user = result.get();
        tryUserLockout(user);
        tryMatchingPassword(user, token.getCredentials());
        return new DetailedAuthToken(user, Collections.emptyList());
    }

    /**
     * Authentication a given {@link Authentication} token
     * @param authToken the authentication token
     * @return the authenticated token if successful
     * @throws AuthenticationException if the token type is unsupported or authentication fails
     */
    @Override
    public DetailedAuthToken authenticate(final Authentication authToken) throws AuthenticationException {
        if(authToken instanceof EmailPasswordAuthToken)
            return authEmailPassword((EmailPasswordAuthToken) authToken);
        throw new AuthenticationCredentialsNotFoundException("Unknown authentication method");
    }

    /**
     * Indicates the provider supports the given authentication class.
     * @param authClass the class of the authentication token
     * @return {@code  true} if the provider supports {@link EmailPasswordAuthToken} {@code  false} otherwise
     */
    @Override
    public boolean supports(Class<?> authClass) {
        return authClass.equals(EmailPasswordAuthToken.class);
    }
}
