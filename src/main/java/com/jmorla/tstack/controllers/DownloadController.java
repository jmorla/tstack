package com.jmorla.tstack.controllers;

import com.jmorla.tstack.models.InstrumentOverview;
import com.jmorla.tstack.models.ProviderRecord;
import com.jmorla.tstack.services.MarketDataProvider;
import com.jmorla.tstack.services.ProviderService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
@RequestMapping("/datasets/download")
public class DownloadController {

    private final ProviderService providerService;
    private final MarketDataProvider marketDataProvider;

    public DownloadController(
            ProviderService providerService,
            @Qualifier("pepperstoneDataProvider") MarketDataProvider marketDataProvider) {
        this.providerService = providerService;
        this.marketDataProvider = marketDataProvider;
    }

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

    /**
     * HTMX endpoint that returns the instrument fragment with available instruments.
     * 
     * <p>This endpoint fetches the list of available instruments from the market data provider
     * and returns the instrument selection fragment for dynamic updates.</p>
     * 
     * @param model the Spring MVC model for passing data to the view
     * @return the instrument fragment view
     * @since 1.0
     */
    @HxRequest
    @GetMapping("/instruments")
    public String getInstruments(Model model) {
        log.debug("Fetching instruments for HTMX request");
        List<InstrumentOverview> instruments = marketDataProvider.getAvailableInstruments();
        model.addAttribute("instruments", instruments);
        return "fragments/dataset :: instrument";
    }
}