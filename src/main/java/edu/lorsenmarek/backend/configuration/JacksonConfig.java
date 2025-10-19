package edu.lorsenmarek.backend.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.lorsenmarek.backend.converter.jackson.InstantDeserializer;
import edu.lorsenmarek.backend.converter.jackson.InstantSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

/**
 * Configuration class for customizing Jackson JSON
 * @author Marek Gromko
 * @version 1.0
 */
@Configuration
public class JacksonConfig {
    /** {@link JacksonConfig} should not be instantiated */
    public JacksonConfig() {}
    /**
     * Customizes the {@link com.fasterxml.jackson.databind.ObjectMapper} used by Spring boot.
     * <p>this method registers custom serializers and deserializers for {@link java.time.Instant}
     * to ensure consistent JSON formating when sending or receiving API data. </p>
     * @return a {@link Jackson2ObjectMapperBuilderCustomizer} bean that applies the custom settings
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer instantJacksonCodec() {
        return builder -> builder
                .serializationInclusion(JsonInclude.Include.ALWAYS)
                .serializerByType(Instant.class, new InstantSerializer())
                .deserializerByType(Instant.class, new InstantDeserializer());
    }
}
