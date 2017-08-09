package edu.tamu.app.controller.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.tamu.app.model.AppUser;
import edu.tamu.app.service.InterceptorService;
import edu.tamu.framework.interceptor.CoreStompInterceptor;
import edu.tamu.framework.model.Credentials;

/**
 * Application Stomp interceptor. 
 * 
 * Checks command, decodes and verifies token, either returns error message 
 * to frontend or continues to controller.
 * 
 */
@Component
public class AppStompInterceptor extends CoreStompInterceptor<AppUser> {

    @Autowired
    private InterceptorService interceptorService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Credentials getAnonymousCredentials() {
        return interceptorService.getAnonymousCredentials();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AppUser confirmCreateUser(Credentials credentials) {
        return interceptorService.confirmCreateUser(credentials);
    }

}
