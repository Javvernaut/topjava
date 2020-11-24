package ru.javawebinar.topjava.web.json.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalTime;

final class TimeConverter implements Converter<String, LocalTime> {

    private static final TimeConverter INSTANCE = new TimeConverter();

    private TimeConverter() {
    }

    public static TimeConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public @Nullable
    LocalTime convert(@Nullable String source) {
        return StringUtils.hasText(source) ? LocalTime.parse(source) : null;
    }
}

