package org.cccs.tfs.security;

import org.cccs.tfs.domain.Principal;
import org.cccs.tfs.finder.PrincipalFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;

/**
 * User: Craig Cook
 * Date: Jun 10, 2010
 * Time: 7:58:43 PM
 */
public class UserAuthProvider implements AuthenticationProvider {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PrincipalFinder finder;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        if (authenticate((String) auth.getPrincipal(), (String) auth.getCredentials())) {
//            auth.setAuthenticated(true);
            return auth;
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass == UsernamePasswordAuthenticationToken.class;
    }

    private boolean authenticate(String user, String password) {
        log.info("Authenticating: " + user);

        try {
            Principal principal = finder.find(user);
            boolean valid = password.equals(principal.getPassword());

            if (!valid) {
                log.error("Authentication failed: User (" + user + ") supplied incorrect password");
            }

            return valid;
        } catch (EntityNotFoundException e) {
            log.error("Authentication failed: User (" + user + ") does not exist");
            return false;
        }
    }
}
