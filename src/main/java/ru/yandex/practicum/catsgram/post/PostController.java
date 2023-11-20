package ru.yandex.practicum.catsgram.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.Constants;
import ru.yandex.practicum.catsgram.post.dto.NewPostDto;
import ru.yandex.practicum.catsgram.post.dto.PostDto;
import ru.yandex.practicum.catsgram.post.service.PostServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostServiceImpl postService;

    @GetMapping("/posts/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<PostDto> getAllByUserId(@Positive @PathVariable Long userId,
                                        @RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive Integer size) {
        return postService.getAllByUserId(userId, PageRequest.of(from / size, size));
    }

    @GetMapping("/posts")
    @ResponseStatus(HttpStatus.OK)
    public List<PostDto> getAll() {
        return postService.getAll();
    }

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(@Valid @RequestBody NewPostDto newPostDto) {
        log.info("Получен запрос на создание нового поста {} от пользователя с id {}", newPostDto, newPostDto.getAuthorId());
        return postService.createPost(newPostDto);
    }

    @DeleteMapping("post/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long postId) {
        postService.deleteById(postId);
    }
}