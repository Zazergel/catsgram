package ru.yandex.practicum.catsgram.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exceptions.PostNotFoundException;
import ru.yandex.practicum.catsgram.exceptions.UserNotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServices {

    private static final Logger log = LoggerFactory.getLogger(PostServices.class);
    private final UserServices userService;
    private final List<Post> posts = new ArrayList<>();
    private static Integer globalId = 0;

    @Autowired
    public PostServices(UserServices userService) {
        this.userService = userService;
    }

    private static Integer getNextId() {
        return globalId++;
    }

    public List<Post> findAll(Integer size, Integer from, String sort) {
        log.debug("Текущее количество постов: {}", posts.size());
        return posts.stream().sorted((p0, p1) -> compare(p0, p1, sort))
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    public List<Post> findAllByUserEmail(String email, Integer size, String sort) {
        return posts.stream()
                .filter(p -> email.equals(p.getAuthor()))
                .sorted((p0, p1) -> compare(p0, p1, sort))
                .limit(size)
                .collect(Collectors.toList());
    }

    public Post create(Post post) {
        User postAuthor = userService.findUserByName(post.getAuthor());
        if (postAuthor == null) {
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден",
                    post.getAuthor()));
        }
        post.setPostId(getNextId());
        posts.add(post);
        log.info("Пользователь с ником: " + post.getAuthor() + " оставил новый пост: " + post);
        return post;
    }

    public Post findPostById(Integer postId) {
        return posts.stream()
                .filter(p -> p.getPostId().equals(postId))
                .findFirst()
                .orElseThrow(() -> new PostNotFoundException(String.format("Пост № %d не найден", postId)));
    }

    private int compare(Post p0, Post p1, String sort) {
        int result = p0.getCreationDate().compareTo(p1.getCreationDate()); //прямой порядок сортировки
        if (sort.equals("desc")) {
            result = -1 * result; //обратный порядок сортировки
        }
        return result;
    }
}