package ru.yandex.practicum.catsgram.user;

import org.mapstruct.Mapper;
import ru.yandex.practicum.catsgram.user.dto.NewUserDto;
import ru.yandex.practicum.catsgram.user.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(NewUserDto newUserDto);

    UserDto toUserDto(User user);
}
