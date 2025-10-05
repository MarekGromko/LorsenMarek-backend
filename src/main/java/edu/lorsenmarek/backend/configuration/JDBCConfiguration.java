package edu.lorsenmarek.backend.configuration;

import edu.lorsenmarek.backend.converter.jdbc.InstantReadingConverter;
import edu.lorsenmarek.backend.converter.jdbc.InstantWritingConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.List;

@Configuration
public class JDBCConfiguration extends AbstractJdbcConfiguration {
    @Bean
    @Override
    @NonNull
    public JdbcCustomConversions jdbcCustomConversions() {
        List<?> converters = Arrays.asList(
                new InstantReadingConverter(),
                new InstantWritingConverter()
        );
        return new JdbcCustomConversions(converters);
    }

}
