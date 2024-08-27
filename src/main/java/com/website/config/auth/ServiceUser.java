package com.website.config.auth;

import com.website.repository.user.model.User;
import com.website.repository.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUser implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String email;
    private List<GrantedAuthority> authorities;


    public static ServiceUser user(User user) {
        return ServiceUser.builder()
                .id(user.getId())
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(AuthorityUtils.createAuthorityList(
                        user.getRoles().stream().map(UserRole::name).toArray(String[]::new))
                )
                .build();
    }

    public static ServiceUser guest() {
        return ServiceUser.builder()
                .id(null)
                .username(null)
                .password(null)
                .email(null)
                .authorities(AuthorityUtils.createAuthorityList(UserRole.GUEST.name()))
                .build();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

