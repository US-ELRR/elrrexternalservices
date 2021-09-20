package com.deloitte.elrr;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan({"com.deloitte.elrr"})
@Import({JacksonAutoConfiguration.class})
//@EnableEncryptableProperties
public class ElrrExternalServicesApplication implements CommandLineRunner{

	public static void main(String[] args) {
	
      ////for localhost testing only		
//	  javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
//	        new javax.net.ssl.HostnameVerifier(){
//
//	            public boolean verify(String hostname,
//	                    javax.net.ssl.SSLSession sslSession) {
//	                if (hostname.equals("localhost")) {
//	                    return true;
//	                }
//	                return false;
//	            }
//	        });
	  
		SpringApplication.run(ElrrExternalServicesApplication.class, args);
	}


    @Override
    public void run(String...args) throws Exception {
    	//this method is not required
	
    }
    
    
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        return modelMapper;
    }
	
}
