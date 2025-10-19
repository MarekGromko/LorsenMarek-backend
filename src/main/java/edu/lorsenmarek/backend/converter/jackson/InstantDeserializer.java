package edu.lorsenmarek.backend.converter.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import edu.lorsenmarek.backend.util.InstantCodecUtil;

import java.io.IOException;
import java.time.Instant;

/**
 * Deserialize a json string field into an {@link Instant}
 *
 * @see InstantCodecUtil
 */
public final class InstantDeserializer extends JsonDeserializer<Instant> {
    /** Create a new {@link InstantDeserializer} */
    public InstantDeserializer() {}
    /**
     * Do the Deserialization
     * @param jp the json parser
     * @param deserializationContext the deserialization contest
     * @return the decoded {@link Instant}
     * @throws IOException when reading from the json parser
     * @throws JacksonException thrown by Jackson
     */
    @Override
    public Instant deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String str = jp.getText().trim();
        return Instant.from(InstantCodecUtil.decode(str));
    }
}
