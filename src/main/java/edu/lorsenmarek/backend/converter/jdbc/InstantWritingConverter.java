package edu.lorsenmarek.backend.converter.jdbc;

import edu.lorsenmarek.backend.util.InstantCodecUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Instant;

@WritingConverter
public final class InstantWritingConverter implements Converter<Instant, String> {
    @Override
    @NonNull
    public String convert(@NonNull Instant instant) {
        return InstantCodecUtil.encode(instant);
    }
}
