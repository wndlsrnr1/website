package com.website.config.auth;

import com.website.repository.model.user.User;
import com.website.repository.user.UserRepository;
import com.website.repository.user.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Profile({"local", "dev"})
@RequiredArgsConstructor
@Component
public class DebuggingAuth {
    private final String EMAIL = "test@naver.com";
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @PostConstruct
    public void init() {
        userRepository.findByEmail(EMAIL)
                .orElseGet(() -> {
                    User user = User.builder()
                            .name("test")
                            .email(EMAIL)
                            .password("test")
                            .roles(List.of(UserRole.USER))
                            .build();
                    return user;
                });
    }

    public String getBearer() {
        return "Bearer " + jwtUtil.generateToken(EMAIL);
    }
}
