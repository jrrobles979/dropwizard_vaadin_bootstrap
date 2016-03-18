package com.sae.event.auth;

import com.google.common.base.Optional;
import com.sae.event.core.Session;
import com.sae.event.core.User;
import com.sae.event.db.QueryParameters;
import com.sae.event.db.SessionDAO;
import com.sae.event.db.UnmanagedHibernateServiceDAO;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Created by ralmeida on 2/18/16.
 */
public class SAEAuthenticator implements Authenticator<String, User> {
    private static final Log log = LogFactory.getLog(SAEAuthenticator.class);
    private UnmanagedHibernateServiceDAO sessionDAO;

    public SAEAuthenticator(UnmanagedHibernateServiceDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    @Override
    public Optional<User> authenticate(String token) throws AuthenticationException {

        log.info("authenticate: " + token);

        sessionDAO.openSession();
        Session currentSession = sessionDAO.findUniqueWithNamedQuery(Session.findByToken, QueryParameters.with("TOKEN", token).parameters());
        if (currentSession != null){
            log.info("authenticated");

            Optional<User> currentUser = Optional.of(currentSession.getUser());
            sessionDAO.closeSession();
            return currentUser;
        }

        log.info("authenticate: not exists" );
        sessionDAO.closeSession();
        return Optional.absent();
    }
}
