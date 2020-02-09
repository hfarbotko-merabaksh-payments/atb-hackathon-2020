package ca.atb.hackathon_2020.client;

import ca.atb.hackathon_2020.RestClient;
import ca.atb.hackathon_2020.domain.Token;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
public class DirectAuthenticationClient {
    @Value("${obp.api.directLoginPath}")
    private String loginPath;

    private final RestClient restClient;

    public Token loginInternal(@RequestHeader("Authorization") String authHeader) {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", APPLICATION_JSON_VALUE);
        headers.add("Authorization", authHeader);

        return restClient.post(loginPath, headers, null, Token.class)
                .orElseThrow(() -> new RuntimeException("Failed to get token"));
    }

    public String login(String username, String password, String consumerKey) {
        val dlData = String.format("DirectLogin username=\"%s\",password=\"%s\",consumer_key=\"%s\"", username, password, consumerKey);
        val token = loginInternal(dlData).getToken();
        return token;
    }
}
