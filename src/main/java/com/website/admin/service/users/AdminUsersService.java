package com.website.admin.service.users;

import com.website.admin.service.users.model.UserDto;
import com.website.common.exception.ClientException;
import com.website.common.exception.ErrorCode;
import com.website.common.repository.user.UserRepository;
import com.website.common.repository.user.model.User;
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
