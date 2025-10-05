package edu.lorsenmarek.backend.util;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class InstantCodecUtils {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneOffset.UTC);
}
