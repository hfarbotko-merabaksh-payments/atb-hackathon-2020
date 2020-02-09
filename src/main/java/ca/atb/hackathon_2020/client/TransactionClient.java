package ca.atb.hackathon_2020.client;

import ca.atb.hackathon_2020.RestClient;
import ca.atb.hackathon_2020.common.NotFoundException;
import ca.atb.hackathon_2020.domain.Transaction;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionClient {
    private final RestClient restClient;
    private final AddAuth addAuth;

    public static final String transactionClient = "https://api.leapos.ca/obp/v4.0.0/my/banks/{bankId}/accounts/{accountId}/transactions";

    public List<Transaction> findTransactionsForDateRange(String bankId, String accountId,
                                                          LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, String> pathVariable = Map.of("bankId", bankId, "accountId", accountId);
        HttpHeaders authHeader = addAuth.createAuthHeader();

        authHeader.add("from_date", startTime.format(DateTimeFormatter.ISO_DATE_TIME));
        authHeader.add( "to_date", endTime.format(DateTimeFormatter.ISO_DATE_TIME));
        authHeader.add("limit", "50");

        Transactions transactions = restClient
                .get(transactionClient, pathVariable, authHeader, null, Transactions.class)
                .orElseThrow(NotFoundException::new);
        return transactions.getTransactions();

    }

    @Data
    public static class Transactions {
        private List<Transaction> transactions;
    }
}
