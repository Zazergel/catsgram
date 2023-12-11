package ru.yandex.practicum.catsgram.post.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.catsgram.Constants;
import ru.yandex.practicum.catsgram.exceptions.ForbiddenException;
import ru.yandex.practicum.catsgram.exceptions.NotFoundException;
import ru.yandex.practicum.catsgram.friendship.service.FriendshipService;
import ru.yandex.practicum.catsgram.like.Like;
import ru.yandex.practicum.catsgram.post.Post;
import ru.yandex.practicum.catsgram.post.PostMapper;
import ru.yandex.practicum.catsgram.post.PostRepository;
import ru.yandex.practicum.catsgram.post.dto.NewPostDto;
import ru.yandex.practicum.catsgram.post.dto.PostDto;
import ru.yandex.practicum.catsgram.post.dto.UpdatePostDto;
import ru.yandex.practicum.catsgram.user.User;
import ru.yandex.practicum.catsgram.user.UserRepository;
import ru.yandex.practicum.catsgram.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostServiceImpl implements PostService {
    PostRepository postRepository;
    UserRepository userRepository;
    FriendshipService friendshipService;
    PostMapper postMapper;

    @Override
    public Page<PostDto> getAllByUserId(Long userId, Pageable pageable) {
        log.info("Вывод всех постов пользователя с id {} и пагинацией {}", userId, pageable);
        checkUserExist(userId);
        List<PostDto> posts = postRepository.findAllByUserId(userId)
                .stream()
                .map(postMapper::toPostDto)
                .sorted(Comparator.comparing(PostDto::getCreatedOn).reversed())
                .collect(Collectors.toList());
        return new PageImpl<>(posts, pageable, posts.size());
    }

    @Override
    public Page<PostDto> getAll(Pageable pageable) {
        log.info("Вывод всех постов от всех пользователей c пагинацией {}", pageable);
        List<PostDto> posts = postRepository.findAll()
                .stream()
                .map(postMapper::toPostDto)
                .sorted(Comparator.comparing(PostDto::getCreatedOn).reversed())
                .collect(Collectors.toList());
        if (posts.isEmpty()) {
            return Page.empty();
        }
        return new PageImpl<>(posts, pageable, posts.size());
    }

    @Override
    public Page<PostDto> getUserFeed(Long userId, Pageable pageable) {
        List<UserDto> friendsOfUser = friendshipService.findFriendsFromUserById(userId, pageable);
        if (friendsOfUser.isEmpty()) {
            return Page.empty();
        }
        List<Long> friendsIds = friendsOfUser
                .stream()
                .map(UserDto::getId)
                .collect(Collectors.toList());
        List<PostDto> postDtos = postRepository.findAllByUserIdIn(friendsIds)
                .stream()
                .map(postMapper::toPostDto)
                .collect(Collectors.toList());
        return new PageImpl<>(postDtos, pageable, postDtos.size());
    }

    @Override
    @Transactional
    public PostDto createPost(NewPostDto newPostDto, Long userId) {
        User author = checkUserExist(userId);
        Set<Like> likes = new HashSet<>();
        return postMapper.toPostDto(
                postRepository.save(postMapper.toPost(newPostDto, author, LocalDateTime.now(), likes)));
    }

    @Override
    @Transactional
    public PostDto pathPostByUserById(Long postId, Long userId, UpdatePostDto updatePostDto) {
        checkUserExist(userId);
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
                postRepository.save(updatedPost));
    }

    @Override
    @Transactional
    public void deleteById(Long postId) {
        log.info("Удаление поста с id {}", postId);
        postRepository.deleteById(postId);
    }

    private User checkUserExist(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + Constants.DOES_NOT_EXIST));
    }
}