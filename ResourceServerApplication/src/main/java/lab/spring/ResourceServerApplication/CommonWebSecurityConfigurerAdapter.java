package lab.spring.ResourceServerApplication;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class CommonWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http //
				.antMatcher("/**") //
				.authorizeRequests() //
				.antMatchers("/public") //
				.permitAll() //
				.anyRequest() //
				.authenticated();
	}
}
