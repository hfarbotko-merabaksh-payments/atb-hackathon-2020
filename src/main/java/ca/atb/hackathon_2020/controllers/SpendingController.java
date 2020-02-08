package ca.atb.hackathon_2020.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/yourspending")
public class SpendingController {

    @GetMapping({"/", ""})
    public String yourSpending(Model model) {


        model.addAttribute("greenValue", 45);
//        model.addAttribute("orangeValue", 33);
//        model.addAttribute("redValue", 34);

        return "yourspending";
    }
}
