package ru.yandex.practicum.catsgram.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.catsgram.user.User;
import ru.yandex.practicum.catsgram.user.UserMapper;
import ru.yandex.practicum.catsgram.user.UserRepository;
import ru.yandex.practicum.catsgram.user.dto.NewUserDto;
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
    public UserDto patchUser(Long userId, NewUserDto newUserDto) {
        User user = userRepository.findUserById(userId);
        if (newUserDto.getName() != null) {
            user.setName(newUserDto.getName());
        }
        if (newUserDto.getBirthday() != null) {
            user.setBirthday(newUserDto.getBirthday());
        }
        if (newUserDto.getEmail() != null) {
            user.setEmail(newUserDto.getEmail());
        }
        log.info("Данные пользователя с id {} обновлены на {}", userId, user);
        return userMapper.toUserDto(userRepository.save(user));
    }


    @Override
    public UserDto getUserById(Long userId) {
        log.info("Вывод пользователя с id {}", userId);
        return userMapper.toUserDto(userRepository.findUserById(userId));
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
}