package ru.yandex.practicum.catsgram.user;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.catsgram.user.dto.NewUserDto;
import ru.yandex.practicum.catsgram.user.dto.UserDto;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class UserMapperImplTest {
    @InjectMocks
    private UserMapperImpl userMapper;

    private final NewUserDto newUserDto = NewUserDto.builder()
            .name("Kate")
            .birthday(LocalDate.now().minusYears(10))
            .email("kate@yandex.ru")
            .build();
    private final User user = User.builder()
            .id(1L)
            .name(newUserDto.getName())
            .birthday(newUserDto.getBirthday())
            .email(newUserDto.getEmail())
            .build();
    private final UserDto userDto = UserDto.builder()
            .id(user.getId())
            .name(user.getName())
            .birthday(user.getBirthday())
            .email(user.getEmail())
            .build();

    @Nested
    class ToUser {
        @Test
        void shouldReturnUser() {
            User result = userMapper.toUser(newUserDto);

            assertNull(result.getId());
            assertEquals(newUserDto.getName(), result.getName());
            assertEquals(newUserDto.getBirthday(), result.getBirthday());
            assertEquals(newUserDto.getEmail(), result.getEmail());
        }

        @Test
        void shouldReturnNull() {
            User result = userMapper.toUser(null);

            assertNull(result);
        }
    }

    @Nested
    class ToUserDto {
        @Test
        void shouldReturnUser() {
            UserDto result = userMapper.toUserDto(user);

            assertEquals(userDto.getId(), result.getId());
            assertEquals(userDto.getBirthday(), result.getBirthday());
            assertEquals(userDto.getName(), result.getName());
            assertEquals(userDto.getEmail(), result.getEmail());
        }

        @Test
        void shouldReturnNull() {
            UserDto result = userMapper.toUserDto(null);

            assertNull(result);
        }
    }

}