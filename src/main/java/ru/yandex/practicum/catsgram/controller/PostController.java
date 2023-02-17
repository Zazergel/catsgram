package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.services.PostServices;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
public class PostController {
    private final PostServices postService;

    @Autowired
    public PostController(PostServices postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public List<Post> findAll(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                              @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                              @RequestParam(value = "sort", defaultValue = "desc", required = false) String sort) {
        if(!(sort.equals("asc") || sort.equals("desc"))){
            throw new IllegalArgumentException();
        }
        if(page < 0 || size <= 0){
            throw new IllegalArgumentException();
        }

        Integer from = page * size;
        return postService.findAll(size, from, sort);
    }

    @GetMapping("/posts/{postId}")
    public Post findById(@PathVariable("postId") @Positive Integer postId) {
        return postService.findPostById(postId);
    }

    @PostMapping(value = "/post")
    public Post create(@RequestBody @Valid Post post) {
        return postService.create(post);
    }
}
