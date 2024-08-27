package com.website.repository.user;

import com.website.repository.user.model.User;

public interface CustomUserRepository {

    void saveUser(User user);

    User findUserByEmail(String email);

    User findByUserId(Long userId);
}
