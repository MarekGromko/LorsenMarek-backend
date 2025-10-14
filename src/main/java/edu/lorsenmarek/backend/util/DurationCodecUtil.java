package edu.lorsenmarek.backend.util;

import java.time.Duration;

public class DurationCodecUtil {
    public static Duration decode(String encodedDuration) {
        if(encodedDuration == null || encodedDuration.isEmpty())
            return Duration.ZERO;
        ///
        var normal = encodedDuration
                .toLowerCase()
                .replaceAll("\\s", "");
        if(!normal.startsWith("p"))
            normal = "p" + normal;
        normal = normal.toUpperCase();
        return Duration.parse(normal);
    }
}
