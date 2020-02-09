package ca.atb.hackathon_2020.ui_models;

import ca.atb.hackathon_2020.domain.Transaction;
import lombok.*;
import org.joda.money.Money;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class SimplifiedTransaction {
    private @NonNull LocalDateTime timeOfTransaction;
    private @NonNull Money amount;

    public static SimplifiedTransaction of(Transaction transaction) {
        return SimplifiedTransaction.builder()
                .amount(transaction.getDetails().getValue())
                .timeOfTransaction(transaction.getDetails().getPostedDate())
                .build();
    }
}
