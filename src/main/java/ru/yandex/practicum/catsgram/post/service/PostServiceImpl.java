package ru.yandex.practicum.catsgram.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.catsgram.post.PostMapper;
import ru.yandex.practicum.catsgram.post.PostRepository;
import ru.yandex.practicum.catsgram.post.dto.NewPostDto;
import ru.yandex.practicum.catsgram.post.dto.PostDto;
import ru.yandex.practicum.catsgram.user.User;
import ru.yandex.practicum.catsgram.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    @Override
    public List<PostDto> getAllByUserId(Long userId, Pageable pageable) {
        log.info("Вывод всех постов пользователя с id {} и пагинацией {}", userId, pageable);
        return postRepository.findAllByUserId(userId, pageable)
                .stream()
                .map(post -> postMapper.toPostDto(post, post.getUser().getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getAll() {
        log.info("Вывод всех постов от всех пользователей");
        return postRepository.findAll()
                .stream()
                .map(post -> postMapper.toPostDto(post, post.getUser().getName()))
                .sorted(Comparator.comparing(PostDto::getCreatedOn))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PostDto createPost(NewPostDto newPostDto) {
        User author = userRepository.findUserById(newPostDto.getAuthorId());
        return postMapper.toPostDto(
                postRepository.save(postMapper.toPost(newPostDto, author, LocalDateTime.now())), author.getName());
    }

    @Override
    @Transactional
    public void deleteById(Long postId) {
        log.info("Удаление поста с id {}", postId);
        postRepository.deleteById(postId);
    }
}