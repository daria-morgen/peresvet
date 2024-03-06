package com.silence.earth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
public class Config {

   private final Logger LOG = LoggerFactory.getLogger(Config.class);

    @Value("${periods.path}")
    private String filePath;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Period[] periods;

    @Bean
    public String loadPeriods() throws IOException {
        LOG.debug("loadPeriods started");
        periods = objectMapper.readValue(new File(filePath), Period[].class);

        return Strings.EMPTY;
    }
}
