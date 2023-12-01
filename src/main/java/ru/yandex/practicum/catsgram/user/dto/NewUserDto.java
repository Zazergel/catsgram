package ru.yandex.practicum.catsgram.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.catsgram.Constants;

import javax.validation.constraints.*;
import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class NewUserDto {
    @NotBlank(message = "Имя пользователя не может быть пустым!")
    @Size(min = Constants.MIN_LENGTH_USERNAME, max = Constants.MAX_LENGTH_USERNAME)
    String name;

    @NotNull(message = "Необходимо указать дату рождения!")
    @PastOrPresent(message = "Кажется, ваш день рождения еще не наступил :)")
    LocalDate birthday;

    @NotBlank(message = "Поле email не может быть пустым!")
    @Email(message = "Некорректный формат email!")
    @Size(min = Constants.MIN_LENGTH_EMAIL, max = Constants.MAX_LENGTH_EMAIL)
    String email;
}
