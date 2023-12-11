package ru.yandex.practicum.catsgram.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;

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
    public Page<UserDto> findAllByIdIn(List<Long> ids, Pageable pageable) {
        log.info("Вывод пользователей по заданному списку id {} с пагинацией {}", ids, pageable);
        if (ids == null) {
            List<UserDto> users = userRepository.findAll()
                    .stream()
                    .map(userMapper::toUserDto)
                    .sorted(Comparator.comparing(UserDto::getId))
                    .collect(Collectors.toList());
            return new PageImpl<>(users, pageable, users.size());
        }
        List<UserDto> listOfUsersByIds = userRepository.findAllByIdIn(ids, pageable)
                .stream()
                .map(userMapper::toUserDto)
                .sorted(Comparator.comparing(UserDto::getId))
                .collect(Collectors.toList());
        if (listOfUsersByIds.isEmpty()) {
            return Page.empty();
        }
        return new PageImpl<>(listOfUsersByIds, pageable, listOfUsersByIds.size());
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