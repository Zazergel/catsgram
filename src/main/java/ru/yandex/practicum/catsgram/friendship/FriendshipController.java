package ru.yandex.practicum.catsgram.friendship;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.Constants;
import ru.yandex.practicum.catsgram.friendship.service.FriendshipService;
import ru.yandex.practicum.catsgram.user.dto.UserDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/friendship")
@RequiredArgsConstructor
@Slf4j
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping("/addFriendRequest")
    @ResponseStatus(HttpStatus.OK)
    public Friendship addFriendshipRequest(@RequestParam Long userId,
                                           @RequestParam Long friendId) {
        log.info("Пользователь с id {} отправил запрос на дружбу пользователю с id {}", userId, friendId);
        return friendshipService.addFriendshipRequest(userId, friendId);
    }

    @PostMapping("/confirmFriendship")
    @ResponseStatus(HttpStatus.OK)
    public Friendship confirmFriendship(@RequestParam Long userId,
                                        @RequestParam Long friendId) {
        log.info("Пользователь с id {} подтвердил запрос на дружбу от пользователя с id {}", userId, friendId);
        return friendshipService.confirmFriendship(userId, friendId);
    }

    @GetMapping("{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getFriendsFromUserById(@Positive @PathVariable Long userId,
                                                @RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive Integer size) {
        log.info("Вывод списка друзей пользователя с id {}", userId);
        return friendshipService.findFriendsFromUserById(userId, PageRequest.of(from / size, size));
    }

    @DeleteMapping("/{userId}/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriendByIdFromUserById(@Positive @PathVariable Long userId,
                                             @Positive @PathVariable Long friendId) {
        log.info("Пользователь с id {} удалил пользователя с id {} из друзей", userId, friendId);
        friendshipService.deleteFriendById(userId, friendId);
    }

    @DeleteMapping("/rejectFriendship")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rejectFriendship(@RequestParam Long userId,
                                 @RequestParam Long friendId) {
        log.info("Пользователь с id {} отклонил запрос на дружбу с пользователем с id {}", userId, friendId);
        friendshipService.rejectFriendship(userId, friendId);
    }

}
