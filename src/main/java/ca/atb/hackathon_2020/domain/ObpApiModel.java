package ca.atb.hackathon_2020.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import ca.atb.hackathon_2020.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

public interface ObpApiModel {

    @Data
    class Transactions {
        private List<Transaction> transactions;
    }

    @Data
    @NoArgsConstructor @AllArgsConstructor
    class Where {
        @JsonProperty("where")
        private Location location;
    }

    @Data
    class TransactionRequestTypes {
        @JsonProperty("transaction_request_types")
        private List<TransactionRequestType> transactionRequests;
    }

    @Data
    class AccountViews {
        private List<AccountView> views;
    }
}
