package ru.yandex.practicum.catsgram.post;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"user", "likes"})
    @NotNull
    List<Post> findAllByUserId(Long userId);

    @EntityGraph(attributePaths = {"user", "likes"})
    @NotNull
    List<Post> findAllByUserIdIn(List<Long> userIds);

    @EntityGraph(attributePaths = {"user", "likes"})
    @NotNull
    List<Post> findAll();
}
