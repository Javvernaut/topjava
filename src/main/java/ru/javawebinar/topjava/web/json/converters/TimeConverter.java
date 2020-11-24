package ru.javawebinar.topjava.web.json.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalTime;

final class TimeConverter implements Converter<String, LocalTime> {

    @Override
    public @Nullable
    LocalTime convert(@Nullable String source) {
        return DateTimeUtil.parseLocalTime(source);
    }
}

