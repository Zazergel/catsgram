package ru.yandex.practicum.catsgram.post.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.catsgram.Constants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class NewPostDto {
    @NotBlank(message = "Описание не может быть пустым!")
    @Size(max = Constants.MAX_LENGTH_POST_DESCRIPTION)
    String description;
    @NotNull(message = "Нельзя опубликовать пост без фотографии!")
    @Size(max = Constants.MAX_LENGTH_PHOTO_URL)
    String photoUrl;
}
