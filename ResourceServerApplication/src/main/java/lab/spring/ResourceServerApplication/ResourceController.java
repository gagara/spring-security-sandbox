package lab.spring.ResourceServerApplication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

	@RequestMapping("/me")
	public String securedEndpoint() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getPrincipal() != null && auth.getPrincipal() instanceof Jwt) {
			return "Hello, " + ((Jwt) auth.getPrincipal()).getClaimAsString("name");
		}
		if (auth.getPrincipal() != null && auth.getPrincipal() instanceof OAuth2AuthenticatedPrincipal) {
			return "Hello, " + ((OAuth2AuthenticatedPrincipal) auth.getPrincipal()).getAttribute("user_name");
		}
		return "Hello";
	}

	@RequestMapping("/public")
	public String publicEndpoint() {
		return "Hello world";
	}
}
