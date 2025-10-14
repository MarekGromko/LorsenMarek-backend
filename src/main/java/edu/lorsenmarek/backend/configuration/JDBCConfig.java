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

/**
 * Configuration class for customizing JDBC data type conversion in Spring Data JDBC.
 * This class extends {@link AbstractJdbcConfiguration} to provide additional converters that allow seamless reading and writing of {@link java.time.Instant}
 * @author Lorsen Lamour
 * @version 1.0
 */
@Configuration
public class JDBCConfig extends AbstractJdbcConfiguration {
    /**
     * Defines custom converters for reading and writing database values
     * @return a {@link JdbcCustomConversions} bean that includes the custom converters
     * used by Spring Data JDBC during persistence operation
     */
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
