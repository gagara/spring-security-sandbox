package lab.spring.OAuthClientApplication;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cloud.security.oauth2.proxy.ProxyAuthenticationProperties;
import org.springframework.cloud.security.oauth2.proxy.ProxyAuthenticationProperties.Route;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * @see org.springframework.cloud.security.oauth2.proxy.OAuth2TokenRelayFilter
 */
public class OAuth2ProxyZuulFilter extends ZuulFilter {

	private static final String ACCESS_TOKEN = "ACCESS_TOKEN";

	private static final String TOKEN_TYPE = "TOKEN_TYPE";

	private OAuth2AuthorizedClientService authorizedClientService;

	private Map<String, Route> routes = new HashMap<String, Route>();

	public OAuth2ProxyZuulFilter(ProxyAuthenticationProperties properties,
			OAuth2AuthorizedClientService authorizedClientService) {
		this.routes = properties.getRoutes();
		this.authorizedClientService = authorizedClientService;
	}

	@Override
	public int filterOrder() {
		return 10;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public boolean shouldFilter() {
		OidcIdToken idToken = SecurityUtils.getIdToken();
		OAuth2AccessToken accessToken = SecurityUtils.getAccessToken(authorizedClientService);

		if (idToken != null || accessToken != null) {
			RequestContext ctx = RequestContext.getCurrentContext();
			if (!isOauth2Route(ctx)) {
				return false;
			}
			ctx.set(ACCESS_TOKEN, idToken != null ? idToken.getTokenValue() : accessToken.getTokenValue());
			ctx.set(TOKEN_TYPE, idToken != null ? "Bearer"
					: accessToken.getTokenType() == null ? "Bearer" : accessToken.getTokenType().getValue());
			return true;
		}
		return false;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.addZuulRequestHeader("authorization", ctx.get(TOKEN_TYPE) + " " + ctx.get(ACCESS_TOKEN));
		return null;
	}

	private boolean isOauth2Route(RequestContext ctx) {
		if (ctx.containsKey("proxy")) {
			String id = (String) ctx.get("proxy");
			if (routes.containsKey(id)) {
				if (Route.Scheme.OAUTH2.matches(routes.get(id).getScheme())) {
					return true;
				}
			}
		}
		return false;
	}
}
