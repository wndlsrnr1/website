package com.website.service.admin.users;

import com.website.exception.ClientException;
import com.website.exception.ErrorCode;
import com.website.repository.user.UserRepository;
import com.website.repository.user.model.User;
import com.website.service.admin.users.model.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUsersService {

    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public UserDto getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ClientException(ErrorCode.BAD_REQUEST, "user not found. userId = " + userId));
        return UserDto.of(user);
    }
}
