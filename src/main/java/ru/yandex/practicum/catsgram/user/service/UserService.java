package ru.yandex.practicum.catsgram.user.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.catsgram.user.User;
import ru.yandex.practicum.catsgram.user.dto.NewUserDto;
import ru.yandex.practicum.catsgram.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(NewUserDto newUserDto);

    UserDto patchUser(Long userId, NewUserDto newUserDto);

    List<UserDto> findAllUsers(Pageable pageable);

    List<User> findAllByIdIn(List<Long> ids, Pageable pageable);

    UserDto getUserById(Long userId);

    void deleteById(Long userId);
}

