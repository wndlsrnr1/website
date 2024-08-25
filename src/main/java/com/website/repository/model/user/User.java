package com.website.repository.model.user;

import com.website.repository.converter.EnumListConverter;
import com.website.repository.user.model.UserRole;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    private String name;

    private String address;

    private String email;

    private String password;

    @Convert(converter = EnumListConverter.class)
    private List<UserRole> roles;
}
