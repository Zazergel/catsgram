package ru.yandex.practicum.catsgram;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String DT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern(DT_FORMAT);

    public static final String DOES_NOT_EXIST = " does not exist!";

    public static final String PAGE_DEFAULT_FROM = "0";
    public static final String PAGE_DEFAULT_SIZE = "10";

    public static final int MAX_LENGTH_POST_DESCRIPTION = 500;
    public static final int MAX_LENGTH_PHOTO_URL = 1000;

    public static final int MIN_LENGTH_USERNAME = 3;
    public static final int MAX_LENGTH_USERNAME = 15;

    public static final int MIN_LENGTH_EMAIL = 6;
    public static final int MAX_LENGTH_EMAIL = 100;

    private Constants() {
    }

}