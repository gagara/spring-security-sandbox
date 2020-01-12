package lab.spring.AuthorizationServerApplication;

import java.util.Map;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

public class CheckTokenAccessTokenConverter implements AccessTokenConverter {
	private final AccessTokenConverter accessTokenConverter;

	CheckTokenAccessTokenConverter() {
		this(new DefaultAccessTokenConverter());
	}

	CheckTokenAccessTokenConverter(AccessTokenConverter accessTokenConverter) {
		this.accessTokenConverter = accessTokenConverter;
	}

	@Override
	public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
		@SuppressWarnings("unchecked")
		Map<String, Object> claims = (Map<String, Object>) this.accessTokenConverter.convertAccessToken(token,
				authentication);

		// gh-1070
		claims.put("active", true); // Always true if token exists and not expired

		return claims;
	}

	@Override
	public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
		return this.accessTokenConverter.extractAccessToken(value, map);
	}

	@Override
	public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
		return this.accessTokenConverter.extractAuthentication(map);
	}
}
