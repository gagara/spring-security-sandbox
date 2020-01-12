package lab.spring.OAuthClientApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.security.oauth2.proxy.ProxyAuthenticationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import(ProxyAuthenticationProperties.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;

	@Autowired
	private ProxyAuthenticationProperties proxyProperties;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.antMatcher("/**")
			.authorizeRequests()
			.antMatchers("/", "/login**")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.oauth2Login();
	}

	@Bean
	public RestTemplate securedRestTemplate() {
		RestTemplate rest = new RestTemplate();
		rest.getInterceptors().add((request, body, execution) -> {

			OidcIdToken idToken = SecurityUtils.getIdToken();
			if (idToken != null) {
				request.getHeaders().setBearerAuth(idToken.getTokenValue());
				return execution.execute(request, body);
			}

			OAuth2AccessToken accessToken = SecurityUtils.getAccessToken(authorizedClientService);
			if (accessToken != null) {
				request.getHeaders().setBearerAuth(accessToken.getTokenValue());
				return execution.execute(request, body);
			}

			return execution.execute(request, body);
		});
		return rest;
	}

	@Bean
	public OAuth2ProxyZuulFilter zuulFilter() {
		return new OAuth2ProxyZuulFilter(proxyProperties, authorizedClientService);
	}
}
