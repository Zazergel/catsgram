package ru.yandex.practicum.catsgram.exceptions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@AllArgsConstructor
public class ErrorResponse {
    String status;
    String reason;
    String message;
    String timestamp;
}