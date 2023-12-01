package ru.yandex.practicum.catsgram.friendship.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.catsgram.friendship.Friendship;
import ru.yandex.practicum.catsgram.user.User;
import ru.yandex.practicum.catsgram.user.dto.UserDto;

import java.util.List;

public interface FriendshipService {

    List<UserDto> findFriendsFromUserById(Long userId, Pageable pageable);

    Friendship addFriendshipRequest(Long userId, Long friendId);

    void deleteFriendById(Long userId, Long friendId);

    Friendship confirmFriendship(Long userId, Long friendId);

    void rejectFriendship(Long userId, Long friendId);

    List<User> checkTwoFriendsExists(Long userId, Long friendId);

    void friendshipValidation(Long userId, Long friendId);

    Friendship checkFriendshipConfirmedExist(Long userId, Long friendId);

    void checkSelfFriend(Long userId, Long friendId);


}

