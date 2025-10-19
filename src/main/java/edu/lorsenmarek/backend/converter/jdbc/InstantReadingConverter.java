package edu.lorsenmarek.backend.converter.jdbc;

import edu.lorsenmarek.backend.util.InstantCodecUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;

import java.time.Instant;

/**
 * Reading converter that transform a {@link String} to an {@link Instant}
 */
@ReadingConverter
public class InstantReadingConverter implements Converter<String, Instant> {
    /** Create a new {@link InstantReadingConverter} */
    public InstantReadingConverter() {}
    /**
     * Do the conversion
     * @param source the string input
     * @return the matching Instant
     */
    @Override
    @NonNull
    public Instant convert(@NonNull String source) {
        return Instant.from(InstantCodecUtil.decode(source));
    }
}
