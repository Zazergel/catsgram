package ru.yandex.practicum.catsgram.post.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.catsgram.post.dto.NewPostDto;
import ru.yandex.practicum.catsgram.post.dto.PostDto;
import ru.yandex.practicum.catsgram.post.dto.UpdatePostDto;


public interface PostService {
    Page<PostDto> getAllByUserId(Long userId, Pageable pageable);

    PostDto createPost(NewPostDto newPostDto, Long userId);

    Page<PostDto> getAll(Pageable pageable);

    void deleteById(Long postId);

    PostDto pathPostByUserById(Long postId, Long userId, UpdatePostDto updatePostDto);

    Page<PostDto> getUserFeed(Long userId, Pageable pageable);
}
