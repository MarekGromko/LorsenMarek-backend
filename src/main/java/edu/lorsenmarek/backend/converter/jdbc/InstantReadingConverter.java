package edu.lorsenmarek.backend.converter.jdbc;

import edu.lorsenmarek.backend.util.InstantCodecUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;

import java.time.Instant;

@ReadingConverter
public class InstantReadingConverter implements Converter<String, Instant> {
    @Override
    @NonNull
    public Instant convert(@NonNull String source) {
        return Instant.from(InstantCodecUtils.FORMATTER.parse(source));
    }
}
