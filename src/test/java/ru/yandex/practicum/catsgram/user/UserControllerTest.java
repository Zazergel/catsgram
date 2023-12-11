package ru.yandex.practicum.catsgram.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.catsgram.user.dto.NewUserDto;
import ru.yandex.practicum.catsgram.user.dto.UserDto;
import ru.yandex.practicum.catsgram.user.service.UserServiceImpl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {

    private final ObjectMapper mapper;
    private final MockMvc mvc;

    @MockBean
    private UserServiceImpl userService;

    private final UserDto userDto1 = UserDto.builder()
            .id(1L)
            .name("Jonny")
            .birthday(LocalDate.now().minusYears(10))
            .email("jonny@yandex.ru")
            .build();
    private final UserDto userDto2 = UserDto.builder()
            .id(2L)
            .name("Garry")
            .birthday(LocalDate.now().minusYears(5))
            .email("garry@yandex.ru")
            .build();
    private NewUserDto newUserDto;

    @Nested
    class Create {
        @BeforeEach
        public void beforeEach() {
            newUserDto = NewUserDto.builder()
                    .name("jonny")
                    .birthday(LocalDate.now().minusYears(10))
                    .email("jonny@yandex.ru")
                    .build();
        }

        @Test
        void shouldCreate() throws Exception {
            when(userService.createUser(ArgumentMatchers.any())).thenReturn(userDto1);

            mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(newUserDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(mapper.writeValueAsString(userDto1)));

            verify(userService, times(1)).createUser(ArgumentMatchers.any());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " "})
        @NullSource
        void shouldReturnBadRequestIfNameIsIncorrect(String value) throws Exception {
            newUserDto.setName(value);

            mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(newUserDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).createUser(ArgumentMatchers.any());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " "})
        @NullSource
        void shouldReturnBadRequestIfEmailIsIncorrect(String value) throws Exception {
            newUserDto.setEmail(value);

            mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(newUserDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).createUser(ArgumentMatchers.any());
        }

        @Test
        void shouldReturnBadRequestIfEmailIsNotValid() throws Exception {
            newUserDto.setEmail("testYandex.ru");

            mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(newUserDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).createUser(ArgumentMatchers.any());
        }
    }

    @Nested
    class GetUser {
        @Test
        void shouldGet() throws Exception {
            when(userService.getUserById(ArgumentMatchers.any()))
                    .thenReturn(userDto1);

            mvc.perform(get("/users/1")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(userDto1)));

            verify(userService, times(1)).getUserById(ArgumentMatchers.any());
        }
    }
}
