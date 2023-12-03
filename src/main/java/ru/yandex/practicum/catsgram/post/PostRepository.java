package ru.yandex.practicum.catsgram.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {


    List<Post> findAllByUserId(Long userId, Pageable pageable);

    List<Post> findAllByUserIdIn(List<Long> ids, Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    List<Post> findAll();
}
