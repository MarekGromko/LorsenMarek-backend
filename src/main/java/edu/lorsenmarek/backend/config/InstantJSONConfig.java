package edu.lorsenmarek.backend.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Configuration
public class InstantSerializerConfig {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneOffset.UTC);
    private static final StdSerializer<Instant> INSTANT_SERIALIZER = new StdSerializer<Instant>(Instant.class) {
        @Override
        public void serialize(Instant instant, JsonGenerator jg, SerializerProvider serializerProvider) throws IOException {
            jg.writeString(FORMATTER.format(instant));
        }
    };
    private static final StdDeserializer<Instant> INSTANT_DESERIALIZER = new StdDeserializer<Instant>(Instant.class) {
        @Override
        public Instant deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
            String str = jp.getText().trim();
            return Instant.from(FORMATTER.parse(str));
        }
    };
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer addInstantSerialization() {
        return builder -> builder
                .serializationInclusion(JsonInclude.Include.ALWAYS)
                .serializers(INSTANT_SERIALIZER)
                .deserializers(INSTANT_DESERIALIZER);
    }
}
