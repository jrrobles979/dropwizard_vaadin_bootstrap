package com.sae.api.auth;

import com.google.common.base.Optional;
import com.sae.api.core.Session;
import com.sae.api.core.User;
import com.sae.api.db.SessionDAO;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Created by ralmeida on 2/18/16.
 */
public class SAEAuthenticator implements Authenticator<String, User> {
    private static final Log log = LogFactory.getLog(SAEAuthenticator.class);
    private SessionDAO sessionDAO;

    public SAEAuthenticator(SessionDAO sessionDAO) {
        log.info("OneWipAuthenticator.constructor");
        this.sessionDAO = sessionDAO;
    }

    @Override
    public Optional<User> authenticate(String token) throws AuthenticationException {

        log.info("authenticate: " + token);
        java.util.Optional<Session> currentSession = sessionDAO.find(token);
        if (currentSession.isPresent()){
            log.info("authenticated");
            Optional<User> currentUser = Optional.of(currentSession.get().getUser());
            return currentUser;
        }

        log.info("authenticate: not exists" );
        return Optional.absent();
    }
}
