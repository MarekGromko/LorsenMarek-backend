package edu.lorsenmarek.backend.converter.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import edu.lorsenmarek.backend.util.InstantCodecUtil;

import java.io.IOException;
import java.time.Instant;

public final class InstantDeserializer extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String str = jp.getText().trim();
        return Instant.from(InstantCodecUtil.decode(str));
    }
}
