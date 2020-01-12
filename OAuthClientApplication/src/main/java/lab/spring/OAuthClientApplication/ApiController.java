package lab.spring.OAuthClientApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping("/me")
	public String me() {
		return restTemplate.getForObject("http://localhost:8081/me", String.class);
	}
}
