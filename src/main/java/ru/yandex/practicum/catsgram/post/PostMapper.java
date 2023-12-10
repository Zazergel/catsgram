package ru.yandex.practicum.catsgram.post;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.catsgram.like.Like;
import ru.yandex.practicum.catsgram.post.dto.NewPostDto;
import ru.yandex.practicum.catsgram.post.dto.PostDto;
import ru.yandex.practicum.catsgram.user.User;
import ru.yandex.practicum.catsgram.user.UserMapper;

import java.time.LocalDateTime;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PostMapper {

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "user", source = "author")
    @Mapping(target = "likes", source = "likes")
    Post toPost(NewPostDto newPostDto, User author, LocalDateTime createdOn, Set<Like> likes);


    @Mapping(target = "author", source = "post.user.name")
    @Mapping(target = "likes", expression = "java((long) post.getLikes().size())")
    PostDto toPostDto(Post post);
}
