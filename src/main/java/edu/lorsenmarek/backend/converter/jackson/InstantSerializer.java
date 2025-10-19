package edu.lorsenmarek.backend.converter.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import edu.lorsenmarek.backend.util.InstantCodecUtil;

import java.io.IOException;
import java.time.Instant;

/**
 * Serialize an {@link Instant} into a Json object
 *
 * @see InstantCodecUtil
 */
public final class InstantSerializer extends JsonSerializer<Instant> {
    /** Create a new {@link InstantSerializer} */
    public InstantSerializer() {}
    /**
     * Serialize the {@link Instant}
     * @param instant the input
     * @param jg the json generator
     * @param serializerProvider the serializer provider
     * @throws IOException When writing into the json generator
     */
    @Override
    public void serialize(Instant instant, JsonGenerator jg, SerializerProvider serializerProvider) throws IOException {
        jg.writeString(InstantCodecUtil.encode(instant));
    }
}
