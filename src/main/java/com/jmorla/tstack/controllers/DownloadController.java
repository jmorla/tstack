package com.jmorla.tstack.controllers;

import com.jmorla.tstack.models.ProviderRecord;
import com.jmorla.tstack.services.ProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Spring MVC controller for handling dataset download-related web requests.
 * 
 * <p>This controller provides endpoints for dataset download functionality,
 * including displaying the download page and handling download operations.</p>
 * 
 * @author Jorge Morla
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/datasets/download")
public class DownloadController {

    private final ProviderService providerService;

    /**
     * Provides a list of providers to all views in this controller.
     * 
     * <p>This method is automatically called for every request to this controller
     * and adds the providers list to the model, making it available to all views.</p>
     * 
     * @return a list of all available providers
     * @since 1.0
     */
    @ModelAttribute("providers")
public List<ProviderRecord> providers() {
        return providerService.getProviders();
    }

    /**
     * Renders the dataset download page.
     * 
     * <p>This endpoint serves the dataset download view which provides
     * functionality for downloading datasets.</p>
     * 
     * @param model the Spring MVC model for passing data to the view
     * @return the logical view name "download"
     * @since 1.0
     */
    @GetMapping
    public String download(Model model) {
        log.debug("Rendering download page");
        return "download";
    }
}