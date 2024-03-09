package com.silence.mars.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ServerConfig {

    @Value("${currentRemoteServer.host}")
    private String currentRemoteServerHost;

}
