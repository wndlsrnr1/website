package com.website.config.auth;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

@Getter
public class UserAuthenticationToken extends AbstractAuthenticationToken {
    private final ServiceUser principal;
    private final String credentials;

    @Builder
    public UserAuthenticationToken(
            ServiceUser serviceUser,
            String credentials,
            Collection<? extends GrantedAuthority> authorities,
            Map<String, String> details
    ) {
        super(authorities);
        this.credentials = credentials;
        this.principal = serviceUser;
        super.setDetails(details);
        super.eraseCredentials();
        super.setAuthenticated(true);
    }

    @Override
    public Map<String, String> getDetails() {
        return (Map<String, String>) super.getDetails();
    }
}

