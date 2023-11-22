package ru.yandex.practicum.catsgram.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.catsgram.Constants;
import ru.yandex.practicum.catsgram.exceptions.NotFoundException;
import ru.yandex.practicum.catsgram.user.User;
import ru.yandex.practicum.catsgram.user.UserMapper;
import ru.yandex.practicum.catsgram.user.UserRepository;
import ru.yandex.practicum.catsgram.user.dto.NewUserDto;
import ru.yandex.practicum.catsgram.user.dto.UpdateUserDto;
import ru.yandex.practicum.catsgram.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto createUser(NewUserDto newUserDto) {
        log.info("Добавление пользователя {}", newUserDto);
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(newUserDto)));
    }

    @Override
    @Transactional
    public UserDto patchUser(Long userId, UpdateUserDto updateUserDto) {
        log.info("Обновление пользователя с id {} ", userId);
        User user = checkUserExist(userId);
        if (updateUserDto.getName() != null
                && !updateUserDto.getName().equals(user.getName())
                && !updateUserDto.getName().isEmpty()) {
            user.setName(updateUserDto.getName());
        } else {
            throw new IllegalArgumentException("New name is incorrect!");
        }
        if (updateUserDto.getEmail() != null
                && !updateUserDto.getEmail().equals(user.getEmail())
                && !updateUserDto.getEmail().isEmpty()) {
            user.setEmail(updateUserDto.getEmail());
        } else {
            throw new IllegalArgumentException("New email is incorrect!");
        }
        log.info("Данные пользователя с id {} обновлены на {}", userId, user);
        return userMapper.toUserDto(userRepository.save(user));
    }


    @Override
    public UserDto getUserById(Long userId) {
        log.info("Вывод пользователя с id {}", userId);
        return userMapper.toUserDto(checkUserExist(userId));
    }

    @Override
    public List<UserDto> findAllUsers(Pageable pageable) {
        log.info("Вывод всех пользователей с пагинацией {}", pageable);
        return userRepository.findAll(pageable).stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<User> findAllByIdIn(List<Long> ids, Pageable pageable) {
        //#TODO
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public void deleteById(Long userId) {
        log.info("Удаление пользователя с id {}", userId);
        userRepository.deleteById(userId);
    }

    private User checkUserExist(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + Constants.DOES_NOT_EXIST));
    }
}