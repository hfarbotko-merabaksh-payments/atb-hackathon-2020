package ca.atb.hackathon_2020.client;

import ca.atb.hackathon_2020.RestClient;
import ca.atb.hackathon_2020.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountClient {
    public static final String accountBaseUrl = "https://api.leapos.ca/obp/v4.0.0/my/banks/{bankId}/accounts/{accountId}/account";

    private final RestClient restClient;
    private final AddAuth addAuth;

    public Optional<Account> getAccount(String bankId, String accountId) {
        Map<String, String> pathVariable = Map.of("bankId", bankId, "accountId", accountId);
        return restClient.get(accountBaseUrl, pathVariable, addAuth.createAuthHeader(), null, Account.class);
    }

}
