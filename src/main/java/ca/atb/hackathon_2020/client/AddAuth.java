package ca.atb.hackathon_2020.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddAuth {
    private final String token;

    public void addAuthHeaders(HttpHeaders headers) {
        String dlHeader = String.format("DirectLogin token=%s", token);
        headers.add("Authorization", dlHeader);
    }

    public HttpHeaders createAuthHeader() {
        HttpHeaders headers = new HttpHeaders();
        addAuthHeaders(headers);
        return headers;
    }
}
