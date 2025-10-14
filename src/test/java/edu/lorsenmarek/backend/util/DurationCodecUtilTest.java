package edu.lorsenmarek.backend.util;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DurationCodecUtilTest {
    @Nested
    class Decode{
        @Test
        void decodeWithStartingP() {
            // arrange
            var encoded = List.of(
                    "P10D",
                    "pt 1H 30M",
                    "PT1M"
            );

            // act
            var decoded = encoded.stream().map(DurationCodecUtil::decode).toList();

            // assert
            assertEquals(decoded.get(0).getSeconds(), 10*24*60*60);
            assertEquals(decoded.get(1).getSeconds(), (60+30) * 60);
            assertEquals(decoded.get(2).getSeconds(), 60);
        }

        @Test
        void decodeWithoutStartingP() {
            // arrange
            var encoded = List.of(
                    "10D",
                    "t 1H 30M",
                    "1D T 1M"
            );

            // act
            var decoded = encoded.stream().map(DurationCodecUtil::decode).toList();

            // assert
            assertEquals(decoded.get(0).getSeconds(), 10*24*60*60);
            assertEquals(decoded.get(1).getSeconds(), (60+30) * 60);
            assertEquals(decoded.get(2).getSeconds(), (24*60*60) + 60);
        }
    }
}