package edu.lorsenmarek.backend.converter.jdbc;

import edu.lorsenmarek.backend.util.InstantCodecUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Instant;

/**
 * Converter that transform an {@link Instant} to a {@link String}
 *
 * @see InstantCodecUtil
 */
@WritingConverter
public final class InstantWritingConverter implements Converter<Instant, String> {
    /** Create a new {@link InstantWritingConverter} */
    public InstantWritingConverter() {}
    /**
     * Convert  {@link Instant} to a {@link String}
     *
     * @param instant the instant to convert
     * */
    @Override
    @NonNull
    public String convert(@NonNull Instant instant) {
        return InstantCodecUtil.encode(instant);
    }
}
