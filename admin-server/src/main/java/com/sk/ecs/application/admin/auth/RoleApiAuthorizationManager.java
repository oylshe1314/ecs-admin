package com.sk.ecs.application.admin.auth;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

public class RoleApiAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        String reqPath = context.getRequest().getServletPath();
        if (reqPath.startsWith("/error")) {
            return new AuthorizationDecision(true);
        }

        Authentication authentication = authenticationSupplier.get();
        if (authentication instanceof AdminAuthToken) {

            if (reqPath.startsWith("/common/")) {
                return new AuthorizationDecision(true);
            }

            for (GrantedAuthority grantedAuthority : ((AdminAuthToken) authentication).getAuthorities()) {
                String apiPath = grantedAuthority.getAuthority();
                if (apiPath.endsWith("/**")) {
                    if (reqPath.startsWith(apiPath.substring(0, apiPath.length() - 3))) {

                        return new AuthorizationDecision(true);
                    }
                } else {
                    if (apiPath.equalsIgnoreCase(reqPath)) {

                        return new AuthorizationDecision(true);
                    }
                }
            }
        }

        return new AuthorizationDecision(false);
    }
}
