package com.deloitte.elrr;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class CacheConfig {
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
            .saml2Metadata(withDefaults())
            .saml2Login(withDefaults());
        return http.build();
    }
}
