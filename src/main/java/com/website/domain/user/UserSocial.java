package com.website.domain.user;

import com.website.domain.user.constance.SocialLoginType;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSocial {
    @Id
    @Column(name = "user_social_id", nullable = false)
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private SocialLoginType socialLoginType;

    private String code;

    private String socialEmail;

    @Builder
    public UserSocial(User user, SocialLoginType socialLoginType, String code, String socialEmail) {
        this.user = user;
        this.socialLoginType = socialLoginType;
        this.code = code;
        this.socialEmail = socialEmail;
    }
}
