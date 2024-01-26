package com.deloitte.elrr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@Configuration
@EnableWebSecurity
public class CacheConfig {

	@Value("${lrs.samlid}")
	private String samlid;
	@Value("${lrs.samlurl}")
	private String samlurl;
	@Bean
	public RelyingPartyRegistrationRepository relyingPartyRegistrations() throws Exception {
		RelyingPartyRegistration relyingPartyRegistration = RelyingPartyRegistrations
				.fromMetadataLocation(samlurl).registrationId(samlid).build();

		return new InMemoryRelyingPartyRegistrationRepository(relyingPartyRegistration);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//		http.authorizeHttpRequests(
//				authorize -> authorize.anyRequest().authenticated());
//		http.formLogin(withDefaults());

		http
				.authorizeRequests()
//				.requestMatchers("/", "/lrsdata").permitAll() // (3)
				.anyRequest().authenticated() // (4)
				.and()
				.formLogin() // (5)
				.loginPage("/login") // (5)
				.permitAll()
				.and()
				.logout() // (6)
				.permitAll()
				.and()
				.httpBasic();
		System.out.println("cache config $$$$$$$$$$1");


//		http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated()).saml2Metadata(withDefaults())
//				.saml2Login(saml2 -> {
//					try {
//						saml2.relyingPartyRegistrationRepository(relyingPartyRegistrations());
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						log.error("SAML login failure occurred");
//						e.printStackTrace();
//					}
//				}).csrf(csrf -> csrf.disable());

//		http.csrf().disable();

		return http.build();
	}

}
