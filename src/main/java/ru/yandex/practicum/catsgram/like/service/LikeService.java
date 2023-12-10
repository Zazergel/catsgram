package ru.yandex.practicum.catsgram.like.service;

public interface LikeService {

    void addLikeToPostByUserId(Long userId, Long postId);

    void removeLikeFromPostByUserId(Long userId, Long postId);
}
