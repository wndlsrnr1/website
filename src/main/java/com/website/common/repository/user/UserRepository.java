package com.website.common.repository.user;

import com.website.common.repository.user.model.SocialType;
import com.website.common.repository.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndSocialType(String email, SocialType socialType);

    Optional<User> findByIdAndSocialType(Long id, SocialType socialType);
}
