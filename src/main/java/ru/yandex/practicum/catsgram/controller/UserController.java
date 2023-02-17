package ru.yandex.practicum.catsgram.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.services.UserServices;
import ru.yandex.practicum.catsgram.model.User;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServices userService;

    @Autowired
    public UserController(UserServices userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/user/{userMail}")
    public User getUserByEmail(@PathVariable String userMail) {
        return userService.findUserByEmail(userMail);
    }

    @PostMapping
    public User addNewUser(@RequestBody @Valid User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateNewUser(@RequestBody @Valid User user) {
        return userService.updateUser(user);
    }
}
