package ru.yandex.practicum.catsgram.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.Constants;
import ru.yandex.practicum.catsgram.user.dto.NewUserDto;
import ru.yandex.practicum.catsgram.user.dto.UpdateUserDto;
import ru.yandex.practicum.catsgram.user.dto.UserDto;
import ru.yandex.practicum.catsgram.user.service.UserServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping("/{userId}")
    public UserDto getUser(@Positive @PathVariable Long userId) {
        return userService.getUserById(userId);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllByIdIn(@RequestParam(required = false) List<Long> ids,
                                      @RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero Integer from,
                                      @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive Integer size) {
        return userService.findAllByIdIn(ids, PageRequest.of(from / size, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserDto newUserDto) {
        log.info("Получен запрос на создание нового пользователя {}", newUserDto);
        return userService.createUser(newUserDto);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto patchUserById(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserDto updateUserDto) {
        log.info("Получен запрос на обновление данных пользователя c id {}", userId);
        return userService.patchUser(userId, updateUserDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long userId) {
        userService.deleteById(userId);
    }
}