package ru.yandex.practicum.catsgram.post.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.catsgram.post.dto.NewPostDto;
import ru.yandex.practicum.catsgram.post.dto.PostDto;

import java.util.List;

public interface PostService {
    List<PostDto> getAllByUserId(Long userId, Pageable pageable);

    PostDto createPost(NewPostDto newPostDto);

    List<PostDto> getAll();

    void deleteById(Long postId);
}
