package lab.spring.OAuthClientApplication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class SecurityUtils {

	public static OAuth2AccessToken getAccessToken(OAuth2AuthorizedClientService authorizedClientService) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
					((OAuth2AuthenticationToken) auth).getAuthorizedClientRegistrationId(), auth.getName());
			if (authorizedClient != null) {
				return authorizedClient.getAccessToken();
			}
		}
		return null;
	}

	public static OidcIdToken getIdToken() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			OAuth2User principal = (OAuth2User) auth.getPrincipal();
			if (principal != null && principal instanceof OidcUser && ((OidcUser) principal).getIdToken() != null) {
				return ((OidcUser) principal).getIdToken();
			}
		}
		return null;
	}
}
