package ca.atb.hackathon_2020.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/yourspending")
public class SpendingController {
    public String yourspending() {
        return "yourspending";
    }
}
