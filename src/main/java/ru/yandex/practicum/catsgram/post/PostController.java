package ru.yandex.practicum.catsgram.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.Constants;
import ru.yandex.practicum.catsgram.post.dto.NewPostDto;
import ru.yandex.practicum.catsgram.post.dto.PostDto;
import ru.yandex.practicum.catsgram.post.dto.UpdatePostDto;
import ru.yandex.practicum.catsgram.post.service.PostServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@Slf4j
public class PostController {
    private final PostServiceImpl postService;

    @GetMapping("/all/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<PostDto> getAllByUserId(@Positive @PathVariable Long userId,
                                        @RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive Integer size) {
        return postService.getAllByUserId(userId, PageRequest.of(from / size, size));
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<PostDto> getAll(@RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero Integer from,
                                @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive Integer size) {
        log.info("Получен запрос на вывод списка всех постов");
        return postService.getAll(PageRequest.of(from / size, size));
    }

    @GetMapping("/feed/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<PostDto> getUserFeed(@Positive @PathVariable Long userId,
                                     @RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero Integer from,
                                     @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive Integer size) {
        log.info("Получен запрос на вывод персональной ленты постов для пользователя с id {}", userId);
        return postService.getUserFeed(userId, PageRequest.of(from / size, size));
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(@PathVariable Long userId,
                              @Valid @RequestBody NewPostDto newPostDto) {
        log.info("Получен запрос на создание нового поста {} от пользователя с id {}", newPostDto, userId);
        return postService.createPost(newPostDto, userId);
    }

    @PatchMapping("/{postId}/byUser/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto pathPostByUserById(@PathVariable Long postId,
                                      @PathVariable Long userId,
                                      @Valid @RequestBody UpdatePostDto updatePostDto) {
        log.info("Получен запрос на  обновление поста с id {} пользователем с id {}", postId, userId);
        return postService.pathPostByUserById(postId, userId, updatePostDto);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long postId) {
        log.info("Получен запрос на удаление поста с id {}", postId);
        postService.deleteById(postId);
    }
}