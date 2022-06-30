/**
 *
 */

package com.deloitte.elrr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import lombok.Generated;

@SpringBootApplication
@EnableEncryptableProperties
@Import({ JacksonAutoConfiguration.class })
public class ElrrExternalServicesApplication {

    /**
     * This for for running application / Entry point for this.
     *
     * @param args String[]
     */
    @Generated
    public static void main(final String[] args) {
       SpringApplication.run(ElrrExternalServicesApplication.class, args);
    }

    /**
     * This for returning the Rest Template.
     *
     * @return RestTemplate
     */
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }



}
