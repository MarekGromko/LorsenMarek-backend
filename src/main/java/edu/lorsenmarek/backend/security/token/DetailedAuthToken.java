package edu.lorsenmarek.backend.security.token;

import edu.lorsenmarek.backend.model.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Custom {@link AbstractAuthenticationToken} that contained detailed user information.
 * @author Lorsen Lamour
 * @version 1.0
 */
public class DetailedAuthToken extends AbstractAuthenticationToken {
    /**
     * The authenticated user associated with this token
     */
    final private User user;

    /**
     * Constructs a new {@code DetailedAuthToken}
     * @param user The authenticated user
     * @param authorities the collection of granted authorities
     */
    public DetailedAuthToken(final User user, Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
    }

    /**
     * Returns the credentials of the authentication token.
     * @return the use password digest
     */
    @Override
    public Object getCredentials() {
        return user.getPwdDigest();
    }

    /**
     * Returns the principal of this authentication token.
     * @return the authenticated {@link User} entity.
     */
    @Override
    public Object getPrincipal() {
        return user;
    }
}
