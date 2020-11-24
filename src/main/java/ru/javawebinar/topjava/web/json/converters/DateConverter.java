package ru.javawebinar.topjava.web.json.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;

final class DateConverter implements Converter<String, LocalDate> {

    @Override
    public @Nullable
    LocalDate convert(@Nullable String source) {
        return DateTimeUtil.parseLocalDate(source);
    }
}

