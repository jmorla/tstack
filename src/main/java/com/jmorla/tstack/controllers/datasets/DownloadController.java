package com.jmorla.tstack.controllers.datasets;

import com.jmorla.tstack.entities.TimeFrame;
import com.jmorla.tstack.models.DownloadForm;
import com.jmorla.tstack.models.SymbolRecord;
import com.jmorla.tstack.services.CtraderApiService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/datasets/download")
public class DownloadController {

    private final CtraderApiService ctraderApiService;


    @ModelAttribute("downloadForm")
    public DownloadForm downloadForm() {
        return new DownloadForm();
    }

    @ModelAttribute("timeframes")
    public TimeFrame[] timeFrames() {
        return TimeFrame.values();
    }

    @ModelAttribute("symbols")
    public List<SymbolRecord> availableSymbols() {
        return ctraderApiService.getAllAvailableSymbols();
    }

    @GetMapping
    public String getPage(Model model) {
        return "datasets/download";
    }

    @HxRequest
    @PostMapping
    public String download(@ModelAttribute @Validated DownloadForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "datasets/download :: downloadDatasetForm";
        }
        
        model.addAttribute("loadingMessage", "Downloading market data, please wait...");
        System.out.println("Received form data: " + form);
        return "fragments/common :: downloadDatasetForm";
    }
}
