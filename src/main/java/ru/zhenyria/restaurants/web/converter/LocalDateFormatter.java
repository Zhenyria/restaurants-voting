package ru.zhenyria.restaurants.web.converter;

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {

    @Override
    public LocalDate parse(String text, Locale locale) {
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException("String of date must not be null or empty");
        }
        return LocalDate.parse(text);
    }

    @Override
    public String print(LocalDate lt, Locale locale) {
        return lt.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
