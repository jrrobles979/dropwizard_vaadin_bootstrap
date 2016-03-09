package com.sae.api.auth;

import com.sae.api.core.User;
import io.dropwizard.auth.Authorizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by ralmeida on 2/18/16.
 */
public class SAEAuthorizer implements Authorizer<User> {
    private static final Log log = LogFactory.getLog(SAEAuthorizer.class);

    public boolean authorize(User principal, String role) {
        log.info("auhtorize: " + principal.getUsername());
        return true;
    }
}
