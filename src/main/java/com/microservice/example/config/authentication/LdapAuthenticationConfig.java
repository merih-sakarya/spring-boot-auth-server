package com.microservice.example.config.authentication;

import com.microservice.example.config.properties.LdapProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.userdetails.PersonContextMapper;

@Configuration
@Profile("LDAP")
public class LdapAuthenticationConfig {

    @Autowired
    private LdapProperties ldapProperties;

    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource("ldap://localhost:" + ldapProperties.getPort() + "/" + ldapProperties.getBaseDn());
        contextSource.afterPropertiesSet();
        return contextSource;
    }

    @Bean
    public AuthenticationManager ldapAuthenticationManager(DefaultSpringSecurityContextSource contextSource) {
        LdapBindAuthenticationManagerFactory factory = new LdapBindAuthenticationManagerFactory(contextSource);
        factory.setUserDnPatterns("uid={0},ou=people");
        factory.setUserDetailsContextMapper(new PersonContextMapper());
        return factory.createAuthenticationManager();
    }
}
