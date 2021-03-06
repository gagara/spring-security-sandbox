package lab.spring.ResourceServerApplication;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@Configuration
@Profile("spring")
public class AuthServerSpringConfiguration extends CommonWebSecurityConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::opaqueToken);
	}
}
