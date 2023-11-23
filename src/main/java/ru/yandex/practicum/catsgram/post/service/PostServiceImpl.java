package ru.yandex.practicum.catsgram.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.catsgram.Constants;
import ru.yandex.practicum.catsgram.exceptions.ForbiddenException;
import ru.yandex.practicum.catsgram.exceptions.NotFoundException;
import ru.yandex.practicum.catsgram.post.Post;
import ru.yandex.practicum.catsgram.post.PostMapper;
import ru.yandex.practicum.catsgram.post.PostRepository;
import ru.yandex.practicum.catsgram.post.dto.NewPostDto;
import ru.yandex.practicum.catsgram.post.dto.PostDto;
import ru.yandex.practicum.catsgram.post.dto.UpdatePostDto;
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
        checkUserExist(userId);
        return postRepository.findAllByUserId(userId, pageable)
                .stream()
                .map(post -> postMapper.toPostDto(post, post.getUser().getName()))
                .sorted(Comparator.comparing(PostDto::getCreatedOn).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getAll() {
        log.info("Вывод всех постов от всех пользователей");
        return postRepository.findAll()
                .stream()
                .map(post -> postMapper.toPostDto(post, post.getUser().getName()))
                .sorted(Comparator.comparing(PostDto::getCreatedOn).reversed())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PostDto createPost(NewPostDto newPostDto, Long userId) {
        User author = checkUserExist(userId);
        return postMapper.toPostDto(
                postRepository.save(postMapper.toPost(newPostDto, author, LocalDateTime.now())), author.getName());
    }

    @Override
    @Transactional
    public void deleteById(Long postId) {
        log.info("Удаление поста с id {}", postId);
        postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post with id " + postId + Constants.DOES_NOT_EXIST));
        postRepository.deleteById(postId);
    }

    @Override
    @Transactional
    public PostDto pathPostByUserById(Long postId, Long userId, UpdatePostDto updatePostDto) {
        User author = checkUserExist(userId);
        Post updatedPost = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post with id " + postId + Constants.DOES_NOT_EXIST));
        if (!updatedPost.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Редактирование поста доступно только автору!");
        }
        if (updatePostDto.getDescription() != null) {
            updatedPost.setDescription(updatePostDto.getDescription());
            updatedPost.setUpdatedOn(LocalDateTime.now());
        }
        return postMapper.toPostDto(
                postRepository.save(updatedPost), author.getName());
    }

    private User checkUserExist(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + Constants.DOES_NOT_EXIST));
    }
}