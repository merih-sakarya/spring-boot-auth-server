package com.microservice.example.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "authorization-server.client.web-client")
public class WebClientProperties {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String postLogoutRedirectUri;
}
