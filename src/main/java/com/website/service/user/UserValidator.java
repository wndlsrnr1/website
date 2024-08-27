package com.website.service.user;

import com.website.exception.ClientException;
import com.website.exception.ErrorCode;
import com.website.repository.user.model.User;
import com.website.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    /**
     * checks null and exists. and return User
     * @param userId
     * @return Entity of User
     */
    public User validateAndGet(Long userId) {
        if (userId == null) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "userId is null");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ClientException(ErrorCode.BAD_REQUEST, "user not found. userId=" + userId)
                );
        return user;
    }

    public void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "user not found. userId = " + userId);
        }
    }
}
