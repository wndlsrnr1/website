package com.website.repository.user;

import com.website.domain.user.User;

public interface UserRepository {
    User findNormalUserByEmailPassword(String emailParam, String passwordParam);

    void saveUser(User user);

    User findUserByEmail(String email);

    User findByUserId(Long userId);
}
