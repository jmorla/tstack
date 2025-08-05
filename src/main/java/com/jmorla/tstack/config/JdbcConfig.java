package com.jmorla.tstack.config;

import com.jmorla.tstack.converters.IntegerToTimeFrameConverter;
import com.jmorla.tstack.converters.TimeFrameToIntegerConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.util.List;

@Configuration
public class JdbcConfig extends AbstractJdbcConfiguration {

    @Override
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(List.of(
                new TimeFrameToIntegerConverter(),
                new IntegerToTimeFrameConverter()
        ));
    }
}