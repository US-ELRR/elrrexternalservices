package com.deloitte.elrr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.saml2.core.Saml2X509Credential;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;

import java.io.InputStream;

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

		Resource resource = new ClassPathResource("mocksaml.crt");
		try (InputStream is = resource.getInputStream()) {
			X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance("X.509")
					.generateCertificate(is);
			// Saml2X509Credential.verification(certificate);

			RelyingPartyRegistration relyingPartyRegistration = RelyingPartyRegistrations
					.fromMetadataLocation(samlurl).registrationId(samlid)
					.signingX509Credentials((signing) -> signing.add(Saml2X509Credential.verification(certificate))).build();

			return new InMemoryRelyingPartyRegistrationRepository(relyingPartyRegistration);
		}
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated()).saml2Metadata(withDefaults())
				.saml2Login(saml2 -> {
					try {
						saml2.relyingPartyRegistrationRepository(relyingPartyRegistrations());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						log.error("SAML login failure occurred");
						e.printStackTrace();
					}
				});

		return http.build();
	}

}
