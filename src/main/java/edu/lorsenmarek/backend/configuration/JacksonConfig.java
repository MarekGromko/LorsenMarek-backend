package edu.lorsenmarek.backend.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.lorsenmarek.backend.converter.jackson.InstantDeserializer;
import edu.lorsenmarek.backend.converter.jackson.InstantSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer instantJacksonCodec() {
        return builder -> builder
                .serializationInclusion(JsonInclude.Include.ALWAYS)
                .serializerByType(Instant.class, new InstantSerializer())
                .deserializerByType(Instant.class, new InstantDeserializer());
    }
}
