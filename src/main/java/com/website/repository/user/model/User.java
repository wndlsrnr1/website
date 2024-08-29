package com.website.repository.user.model;
import com.website.repository.converter.EnumListConverter;
import com.website.repository.user.model.UserRole;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "users")
@EntityListeners(value = AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    private String name;

    private String address;

    private String email;

    private String password;

    private String socialUnique;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Convert(converter = EnumListConverter.class)
    private List<UserRole> roles;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
