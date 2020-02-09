package ca.atb.hackathon_2020.controllers;

import ca.atb.hackathon_2020.client.TransactionClient;
import ca.atb.hackathon_2020.domain.Transaction;
import ca.atb.hackathon_2020.ui_models.SimplifiedTransaction;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/spendingpatterns")
@RequiredArgsConstructor
public class SpendingController {
    private final TransactionClient transactionClient;
    private final static String accountId = "9043177494179-dc157082-d07";

    @GetMapping({"/", ""})
    public String spendingPatterns(Model model) {
        model.addAttribute("greenValue", 45);

        List<Transaction> transactionsForDateRange = transactionClient.findTransactionsForDateRange("7e583941844b2e4ed0b83c629add971",
                accountId, LocalDateTime.now().minusDays(1), LocalDateTime.now());

        Map<String, List<SimplifiedTransaction>> transactionByType = new HashMap<>();

        transactionsForDateRange
                .forEach(t -> {
                    if (transactionByType.containsKey(t.getDetails().getType().toUpperCase())) {
                        transactionByType.get(t.getDetails().getType().toUpperCase()).add(SimplifiedTransaction.of(t));
                    } else {
                        List<SimplifiedTransaction> list = new ArrayList<>();
                        list.add(SimplifiedTransaction.of(t));
                        transactionByType.put(t.getDetails().getType().toUpperCase(), list);
                    }
                });



        return "spendingPatterns";
    }

    @GetMapping({"/averagehealthyspending/", "/averagehealthyspending"})
    public String averagehealthyspending() {
        return "spendingpatterns/averagehealthyspending";
    }

    @GetMapping({"/totalSpending/", "/totalSpending"})
    public String totalMoneySpent(Model model) {
        List<Transaction> transactionsForDateRange = transactionClient.findTransactionsForDateRange("7e583941844b2e4ed0b83c629add971",
                "9043177494179-dc157082-d07", LocalDateTime.now().minusDays(31), LocalDateTime.now()).stream()
                .filter(t -> t.getOwnAccount().getId().equals(accountId) && t.getDetails().getValue().getAmount().signum() < 0)
                .collect(Collectors.toList());

        Map<String, BigDecimal> countByType = transactionsForDateRange.stream()
                .map(t -> ImmutablePair.of(t.getDetails().getType(), t.getDetails().getValue().getAmount().abs()))
                .collect(
                        Collectors.groupingBy(ImmutablePair::getLeft,
                                Collectors.reducing(BigDecimal.ZERO, ImmutablePair::getRight, BigDecimal::add)
                        )
                );

        List<String> headers = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        countByType.forEach((k, v) -> {
            headers.add(k.toUpperCase().replaceAll("_", " "));
            values.add(v.doubleValue());
        });

        model.addAttribute("headers", headers);
        model.addAttribute("values", values);

        return "spendingpatterns/totalSpending";
    }

    @GetMapping({"/habitsbyday/", "/habitsbyday"})
    public String habitsbyday() {
        return "spendingpatterns/habitsbyday";
    }

}
