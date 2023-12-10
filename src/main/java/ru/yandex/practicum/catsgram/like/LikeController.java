package ru.yandex.practicum.catsgram.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.like.service.LikeService;

import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@Transactional(readOnly = true)
@RequestMapping("/like")
@Slf4j
public class LikeController {

    private final LikeService likeService;


    @PostMapping("/add/{userId}/{postId}")
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    public void addLikeToFilmByUserId(@Positive @PathVariable Long userId,
                                      @Positive @PathVariable Long postId) {
        log.info("Пользователь с id {} отправил запрос на то, чтобы поставить мурк посту с id {}", userId, postId);
        likeService.addLikeToPostByUserId(userId, postId);
    }

    @DeleteMapping("/remove/{userId}/{postId}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLikeFromPostByUserId(@Positive @PathVariable Long userId,
                                           @Positive @PathVariable Long postId) {
        log.info("Пользователь с id {} отправил запрос на удаление мурка посту с id {}", userId, postId);
        likeService.removeLikeFromPostByUserId(userId, postId);
    }
}
