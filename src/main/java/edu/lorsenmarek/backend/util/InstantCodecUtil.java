package edu.lorsenmarek.backend.util;

import edu.lorsenmarek.backend.converter.jdbc.InstantWritingConverter;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Codec Utility for {@link Instant}
 */
public final class InstantCodecUtil {
    private InstantCodecUtil() {}
    /**
     * The Instant string format yyyy-MM-dd HH:mm:ss on UTC
     */
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneOffset.UTC);

    /**
     * Decode a {@link String} inta an {@link Instant}
     * @param instantString the string to decode
     * @return an instant matching the source string
     */
    public static Instant decode(String instantString) {
        return Instant.from(FORMATTER.parse(instantString));
    }

    /**
     * Encode a {@link Instant} inta a {@link String}
     * @param instant the instant to encode
     * @return a string encoding the instant
     */
    public static String encode(Instant instant) {
        return FORMATTER.format(instant);
    }
}
