package ru.yandex.practicum.catsgram.friendship;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM FRIENDSHIP f " +
            "WHERE (f.user_id = :userId AND f.friend_id = :friendId) " +
            "OR (f.user_id = :friendId AND f.friend_id = :userId)")
    Optional<Friendship> findByUserIdAndFriendId(Long userId, Long friendId);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM FRIENDSHIP f " +
            "WHERE ((f.user_id = :userId AND f.friend_id = :friendId) " +
            "OR (f.user_id = :friendId AND f.friend_id = :userId)) AND f.confirmed= :confirmed")
    Optional<Friendship> findByUserIdAndFriendIdAndConfirmed(Long userId, Long friendId, boolean confirmed);

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM FRIENDSHIP f " +
            "WHERE (f.user_id = :userId " +
            "OR f.friend_id = :userId) AND f.confirmed = true")
    List<Friendship> findByUserIdOrFriendId(Long userId);
}
