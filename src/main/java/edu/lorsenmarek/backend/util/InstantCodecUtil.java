package edu.lorsenmarek.backend.util;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class InstantCodecUtil {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneOffset.UTC);
    public static Instant decode(String instantString) {
        return Instant.from(FORMATTER.parse(instantString));
    }
    public static String encode(Instant instant) {
        return FORMATTER.format(instant);
    }
}
