package com.microservice.example.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "authorization-server.client.core-client")
public class CoreClientProperties {

    private String clientId;
    private String clientSecret;
}
