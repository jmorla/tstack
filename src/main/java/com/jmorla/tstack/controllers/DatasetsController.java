package com.jmorla.tstack.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/datasets")
public class DatasetsController {

    @GetMapping
    public String datasets(Model model) {
        model.addAttribute("activeMenu", "datasets");
        return "datasets";
    }
}