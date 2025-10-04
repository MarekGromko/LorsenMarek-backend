package edu.lorsenmarek.backend.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.lorsenmarek.backend.converter.jackson.InstantDeserializer;
import edu.lorsenmarek.backend.converter.jackson.InstantSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer instantJacksonCodec() {
        return builder -> builder
                .serializationInclusion(JsonInclude.Include.ALWAYS)
                .serializers(new InstantSerializer())
                .deserializers(new InstantDeserializer());
    }
}
