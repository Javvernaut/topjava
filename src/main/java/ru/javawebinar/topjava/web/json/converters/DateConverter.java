package ru.javawebinar.topjava.web.json.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

final class DateConverter implements Converter<String, LocalDate> {

    @Override
    public @Nullable
    LocalDate convert(@Nullable String source) {
        return StringUtils.hasText(source) ? LocalDate.parse(source) : null;
    }
}

