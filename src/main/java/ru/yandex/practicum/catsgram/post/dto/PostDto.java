package ru.yandex.practicum.catsgram.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.catsgram.Constants;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PostDto {
    Long id;
    String author;
    @JsonFormat(pattern = Constants.DT_FORMAT, shape = JsonFormat.Shape.STRING)
    LocalDateTime createdOn;
    @JsonFormat(pattern = Constants.DT_FORMAT, shape = JsonFormat.Shape.STRING)
    LocalDateTime updatedOn;
    String description;
    String photoUrl;
}
