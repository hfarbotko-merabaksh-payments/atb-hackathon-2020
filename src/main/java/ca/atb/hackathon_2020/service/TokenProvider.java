package ca.atb.hackathon_2020.service;

import ca.atb.hackathon_2020.client.DirectAuthenticationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@RequiredArgsConstructor
public class TokenProvider {
    private final DirectAuthenticationClient directAuthenticationClient;

    @Value("${obp.api.user}")
    private String user;

    @Value("${obp.api.password}")
    private String password;

    @Value("${obp.api.consumerKey}")
    private String consumerKey;

    @Bean
    @Scope("singleton")
    public String token() {
        return directAuthenticationClient.login(user, password, consumerKey);
    }
}
