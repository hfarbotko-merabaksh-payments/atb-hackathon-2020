package ca.atb.hackathon_2020.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/spendingpatterns")
public class SpendingController {

    @GetMapping({"/", ""})
    public String spendingPatterns(Model model) {
        model.addAttribute("greenValue", 45);
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
