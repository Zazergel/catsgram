package ru.yandex.practicum.catsgram.post;

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
import ru.yandex.practicum.catsgram.post.dto.NewPostDto;
import ru.yandex.practicum.catsgram.post.dto.PostDto;
import ru.yandex.practicum.catsgram.post.service.PostServiceImpl;
import ru.yandex.practicum.catsgram.user.service.UserServiceImpl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PostControllerTest {

    private final ObjectMapper mapper;
    private final MockMvc mvc;

    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private PostServiceImpl postService;


    private final PostDto postDto1 = PostDto.builder()
            .id(1L)
            .author("test author name")
            .description("test description")
            .photoUrl("test photo url")
            .createdOn(LocalDateTime.now())
            .likes(null)
            .build();
    private NewPostDto newPostDto;

    @Nested
    class Create {
        @BeforeEach
        public void beforeEach() {
            newPostDto = NewPostDto.builder()
                    .description("test description")
                    .photoUrl("test photo url")
                    .build();
        }

        @Test
        void shouldCreate() throws Exception {
            when(postService.createPost(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(postDto1);

            mvc.perform(post("/post/1")
                            .content(mapper.writeValueAsString(newPostDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(mapper.writeValueAsString(postDto1)));

            verify(postService, times(1)).createPost(ArgumentMatchers.any(), ArgumentMatchers.any());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " "})
        @NullSource
        void shouldReturnBadRequestIfDescriptionIsIncorrect(String value) throws Exception {
            newPostDto.setDescription(value);

            mvc.perform(post("/post/1")
                            .content(mapper.writeValueAsString(newPostDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(postService, never()).createPost(ArgumentMatchers.any(), ArgumentMatchers.any());
        }

        @ParameterizedTest
        @ValueSource(strings = {"description"})
        void shouldReturnBadRequestIfDescriptionIsOverSize(String value) throws Exception {
            newPostDto.setDescription(value.repeat(100));

            mvc.perform(post("/post/1")
                            .content(mapper.writeValueAsString(newPostDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(postService, never()).createPost(ArgumentMatchers.any(), ArgumentMatchers.any());
        }

    }

}