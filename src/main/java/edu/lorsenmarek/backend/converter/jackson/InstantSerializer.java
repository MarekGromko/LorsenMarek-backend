package edu.lorsenmarek.backend.converter.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import edu.lorsenmarek.backend.util.InstantCodecUtil;

import java.io.IOException;
import java.time.Instant;

public final class InstantSerializer extends JsonSerializer<Instant> {
    @Override
    public void serialize(Instant instant, JsonGenerator jg, SerializerProvider serializerProvider) throws IOException {
        jg.writeString(InstantCodecUtil.encode(instant));
    }
}
