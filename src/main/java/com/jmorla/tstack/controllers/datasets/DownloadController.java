package com.jmorla.tstack.controllers.datasets;

import com.jmorla.tstack.entities.Symbol;
import com.jmorla.tstack.entities.TimeFrame;
import com.jmorla.tstack.repositories.SymbolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/datasets/download")
public class DownloadController {


    @GetMapping
    public String getPage(Model model) {
        List<Symbol> symbols = List.of();
        model.addAttribute("symbols", symbols);
        model.addAttribute("timeframes", TimeFrame.values());
        return "datasets/download";
    }

    @PostMapping
    public String download(@RequestParam List<String> symbols, @RequestParam TimeFrame timeframe) {
        //TODO: Implement download logic
        return "redirect:/datasets/download";
    }
}
