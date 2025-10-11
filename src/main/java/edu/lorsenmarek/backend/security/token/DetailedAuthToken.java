package edu.lorsenmarek.backend.security.token;

import edu.lorsenmarek.backend.model.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class DetailedAuthToken extends AbstractAuthenticationToken {
    final private User user;
    public DetailedAuthToken(final User user, Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
    }
    @Override
    public Object getCredentials() {
        return user.getPwdDigest();
    }
    @Override
    public Object getPrincipal() {
        return user;
    }
}
