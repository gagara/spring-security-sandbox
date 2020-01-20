package lab.spring.OAuthClientApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class WebController {

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/home")
	@PreAuthorize("hasAnyAuthority('SCOPE_user_info','ROLE_USER')")
	public String homePage(Model model) throws JsonProcessingException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		model.addAttribute("name", auth.getName());

		OAuth2AccessToken accessToken = SecurityUtils.getAccessToken(authorizedClientService);
		if (accessToken != null) {
			model.addAttribute("access_token", mapper.writeValueAsString(accessToken));

		}

		OAuth2RefreshToken refreshToken = SecurityUtils.getRefreshToken(authorizedClientService);
		if (refreshToken != null) {
			model.addAttribute("refresh_token", mapper.writeValueAsString(refreshToken));

		}

		OidcIdToken idToken = SecurityUtils.getIdToken();
		if (idToken != null) {
			model.addAttribute("id_token", mapper.writeValueAsString(idToken));

		}

		return "home";
	}

	@RequestMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String admin() {
		return "none";
	}
}
