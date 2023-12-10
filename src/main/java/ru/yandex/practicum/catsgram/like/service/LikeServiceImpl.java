package ru.yandex.practicum.catsgram.like.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.catsgram.exceptions.NotFoundException;
import ru.yandex.practicum.catsgram.like.Like;
import ru.yandex.practicum.catsgram.like.LikeRepository;
import ru.yandex.practicum.catsgram.post.PostRepository;
import ru.yandex.practicum.catsgram.user.UserRepository;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LikeServiceImpl implements LikeService {

    UserRepository userRepository;
    PostRepository postRepository;
    LikeRepository likeRepository;

    @Override
    public void addLikeToPostByUserId(Long userId, Long postId) {
        userRepository.findById(userId);
        postRepository.findById(postId);
        Like like = new Like();
        like.setUserId(userId);
        like.setPostId(postId);
        likeRepository.save(like);
        log.info("Пользователь с id {} поставил мурк посту с Id {}", userId, postId);
    }

    @Override
    public void removeLikeFromPostByUserId(Long userId, Long postId) {
        Optional<Like> like = likeRepository.findByUserIdAndPostId(userId, postId);
        if (like.isEmpty()) {
            throw new NotFoundException("Такого мурка не существует!");
        }
        likeRepository.deleteById(like.get().getId());
    }
}
