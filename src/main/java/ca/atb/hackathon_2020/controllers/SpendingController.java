package ca.atb.hackathon_2020.controllers;

import ca.atb.hackathon_2020.client.TransactionClient;
import ca.atb.hackathon_2020.domain.Transaction;
import ca.atb.hackathon_2020.ui_models.SimplifiedTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/spendingpatterns")
@RequiredArgsConstructor
public class SpendingController {
    private final TransactionClient transactionClient;

    @GetMapping({"/", ""})
    public String spendingPatterns(Model model) {
        model.addAttribute("greenValue", 45);

        List<Transaction> transactionsForDateRange = transactionClient.findTransactionsForDateRange("7e583941844b2e4ed0b83c629add971",
                "9043177494179-dc157082-d07", LocalDateTime.now().minusDays(5), LocalDateTime.now());

        Map<String, List<SimplifiedTransaction>> transactionByType = new HashMap<>();

        transactionsForDateRange
                .forEach(t -> {
                    if (transactionByType.containsKey(t.getDetails().getType())) {
                        transactionByType.get(t.getDetails().getType()).add(SimplifiedTransaction.of(t));
                    } else {
                        List<SimplifiedTransaction> list = new ArrayList<>();
                        list.add(SimplifiedTransaction.of(t));
                        transactionByType.put(t.getDetails().getType(), list);
                    }
                });

        Map<String, Long> collect = transactionsForDateRange.stream()
                .map(t -> t.getDetails().getType())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return "spendingPatterns";
    }

    @GetMapping({"/averagehealthyspending/", "/averagehealthyspending"})
    public String averagehealthyspending() {
        return "spendingpatterns/averagehealthyspending";
    }

    @GetMapping({"/habitsbyday/", "/habitsbyday"})
    public String habitsbyday() {
        return "spendingpatterns/habitsbyday";
    }

}
