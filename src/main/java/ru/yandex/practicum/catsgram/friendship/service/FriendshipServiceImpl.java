package ru.yandex.practicum.catsgram.friendship.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.catsgram.Constants;
import ru.yandex.practicum.catsgram.exceptions.ForbiddenException;
import ru.yandex.practicum.catsgram.exceptions.NotFoundException;
import ru.yandex.practicum.catsgram.friendship.Friendship;
import ru.yandex.practicum.catsgram.friendship.FriendshipRepository;
import ru.yandex.practicum.catsgram.user.User;
import ru.yandex.practicum.catsgram.user.UserMapper;
import ru.yandex.practicum.catsgram.user.UserRepository;
import ru.yandex.practicum.catsgram.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
@Slf4j
public class FriendshipServiceImpl implements FriendshipService {

    UserRepository userRepository;
    FriendshipRepository friendshipRepository;
    UserMapper userMapper;

    public List<UserDto> findFriendsFromUserById(Long userId, Pageable pageable) {
        List<Friendship> listOfFriendships = friendshipRepository.findByUserIdOrFriendId(userId);
        if (listOfFriendships.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> friendsIds = listOfFriendships
                .stream()
                .flatMap(f -> Stream.of(f.getFriendId(), f.getUserId()))
                .filter(id -> !id.equals(userId))
                .collect(Collectors.toList());
        return userRepository.findAllByIdIn(friendsIds, pageable)
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Friendship addFriendshipRequest(Long userId, Long friendId) {
        checkSelfFriend(userId, friendId);
        List<User> foundedUsers = checkTwoFriendsExists(userId, friendId);
        User user = foundedUsers.get(0);
        User friend = foundedUsers.get(1);
        friendshipValidation(userId, friendId);
        Friendship friendship = new Friendship();
        friendship.setUserId(user.getId());
        friendship.setFriendId(friend.getId());
        return friendshipRepository.save(friendship);
    }

    @Transactional
    public Friendship confirmFriendship(Long userId, Long friendId) {
        checkSelfFriend(userId, friendId);
        checkTwoFriendsExists(userId, friendId);
        Friendship friendship = checkFriendshipConfirmedExist(userId, friendId);
        if (!userId.equals(friendship.getUserId())) {
            throw new ForbiddenException("Подтверждать дружбу может только тот, кому отправлен запрос");
        }
        friendship.setConfirmed(true);
        return friendship;
    }

    @Transactional
    public void rejectFriendship(Long userId, Long friendId) {
        checkTwoFriendsExists(userId, friendId);
        Friendship friendship = checkFriendshipConfirmedExist(userId, friendId);
        friendshipRepository.deleteById(friendship.getId());
    }

    @Transactional
    public void deleteFriendById(Long userId, Long friendId) {
        Optional<Friendship> friendship = friendshipRepository.findByUserIdAndFriendIdAndConfirmed(userId, friendId, true);
        if (friendship.isEmpty()) {
            throw new NotFoundException("Такой дружбы не существует!");
        }
        friendshipRepository.deleteById(friendship.get().getId());

    }

    public List<User> checkTwoFriendsExists(Long userId, Long friendId) {
        List<User> foundedUsers = userRepository.findAllByIdIn(List.of(userId, friendId),
                PageRequest.of(0 / 2, 2));
        if (foundedUsers.size() < 2) {
            throw new NotFoundException("Some user" + Constants.DOES_NOT_EXIST);
        }
        return foundedUsers;
    }

    public void friendshipValidation(Long userId, Long friendId) {
        if (friendshipRepository.findByUserIdAndFriendId(userId, friendId).isPresent()) {
            throw new ForbiddenException("Такая дружба уже существует или запрос от " +
                    "одного из пользователей ожидает подтверждения!");
        }
    }

    public Friendship checkFriendshipConfirmedExist(Long userId, Long friendId) {
        if (friendshipRepository.findByUserIdAndFriendIdAndConfirmed(userId, friendId, true).isPresent()) {
            throw new ForbiddenException("Friendship between user id " + userId + " and user id " +
                    friendId + " is already confirmed!");
        }
        return friendshipRepository
                .findByUserIdAndFriendIdAndConfirmed(userId, friendId, false)
                .orElseThrow(() -> new NotFoundException("Запросов на дружбу не найдено!"));
    }

    public void checkSelfFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new ForbiddenException("Нельзя добавить в друзья себя самого!");
        }
    }

}
