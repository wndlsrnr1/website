package com.website.config.auth;

import com.website.common.repository.user.model.UserRole;
import com.website.utils.common.constance.KaKaoLoginConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity(proxyTargetClass = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        //.requestMatchers("/h2-console/**", "/h2-console", "/error")
                        .requestMatchers(
                                url("/h2console/**"),
                                url("/h2-console"),
                                url("/error"),
                                url(KaKaoLoginConstant.AUTH_URL.getValue()),
                                url("/auth/user/login"),
                                url("/auth/users/register")
                        )
                        .permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/admin/**"))
                        .hasAuthority(UserRole.ADMIN.name())
                        .anyRequest()
                        .authenticated()
                )

                .csrf(AbstractHttpConfigurer::disable)
                .headers(header -> header.frameOptions(option -> option.disable()))
                .addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .rememberMe(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)

        ;

        return http.build();
    }

    private AntPathRequestMatcher url(String urlParam) {
        return new AntPathRequestMatcher(urlParam);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

