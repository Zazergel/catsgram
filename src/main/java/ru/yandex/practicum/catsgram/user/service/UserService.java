package ru.yandex.practicum.catsgram.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.catsgram.user.dto.NewUserDto;
import ru.yandex.practicum.catsgram.user.dto.UpdateUserDto;
import ru.yandex.practicum.catsgram.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(NewUserDto newUserDto);

    UserDto patchUser(Long userId, UpdateUserDto updateUserDto);

    Page<UserDto> findAllByIdIn(List<Long> ids, Pageable pageable);

    UserDto getUserById(Long userId);

    void deleteById(Long userId);
}

