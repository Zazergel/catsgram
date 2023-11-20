package ru.yandex.practicum.catsgram.post;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.catsgram.post.dto.NewPostDto;
import ru.yandex.practicum.catsgram.post.dto.PostDto;
import ru.yandex.practicum.catsgram.user.User;
import ru.yandex.practicum.catsgram.user.UserMapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PostMapper {

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "user", source = "author")
    Post toPost(NewPostDto newPostDto, User author, LocalDateTime createdOn);

    @Mapping(target = "author", source = "user")
    PostDto toPostDto(Post post, String user);
}
