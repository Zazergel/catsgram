package ru.yandex.practicum.catsgram.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.catsgram.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UpdateUserDto {
    @NotBlank
    @Size(min = Constants.MIN_LENGTH_USERNAME, max = Constants.MAX_LENGTH_USERNAME)
    String name;

    @NotBlank
    @Email
    @Size(min = Constants.MIN_LENGTH_EMAIL, max = Constants.MAX_LENGTH_EMAIL)
    String email;
}
