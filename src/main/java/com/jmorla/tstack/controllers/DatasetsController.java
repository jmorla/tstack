package com.jmorla.tstack.controllers;

import com.jmorla.tstack.config.ApplicationProperties;
import com.jmorla.tstack.ctrader.CtraderOpenApiFacade;
import com.jmorla.tstack.mappers.CtraderMapper;
import com.jmorla.tstack.models.InstrumentOverview;
import com.jmorla.tstack.models.ProviderRecord;
import com.jmorla.tstack.models.DatasetSearchRequest;
import com.jmorla.tstack.models.PageableRequest;
import com.jmorla.tstack.services.DatasetService;
import com.jmorla.tstack.services.ProviderService;
import com.jmorla.tstack.utils.PaginationViewHelper;
import com.jmorla.tstack.utils.StringUtils;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

/**
 * Spring MVC controller for handling dataset-related web requests.
 * 
 * <p>This controller provides endpoints for dataset management functionality,
 * including displaying dataset pages and handling HTMX-powered data table requests
 * with pagination support.</p>
 * 
 * @author Jorge Morla
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/datasets")
public class DatasetsController {

    private final DatasetService datasetService;
    private final ProviderService providerService;
    private final CtraderOpenApiFacade ctraderFacade;
    private final ApplicationProperties applicationProperties;

    /**
     * Provides the list of all providers for all controller methods.
     * 
     * <p>This method is annotated with {@code @ModelAttribute} to automatically
     * add the providers list to all views rendered by this controller.</p>
     * 
     * @return the list of provider records
     * @since 1.0
     */
    @ModelAttribute("providers")
    public List<ProviderRecord> providers() {
        return providerService.getProviders();
    }

    /**
     * Renders the main datasets page.
     * 
     * <p>This endpoint serves the primary datasets view which typically contains
     * the layout and initial structure for dataset management functionality.</p>
     * 
     * @param model the Spring MVC model for passing data to the view
     * @return the logical view name "datasets"
     * @since 1.0
     */
    @GetMapping
    public String datasets(Model model) {
        log.debug("Rendering datasets page");
        return "datasets";
    }

    /**
     * Handles HTMX requests for the datasets data table with pagination.
     * 
     * <p>This endpoint provides server-side pagination for datasets and returns
     * a fragment that can be dynamically loaded into the page via HTMX. The response
     * includes the dataset records, pagination metadata, and total count.</p>
     * 
     * @param request the datatable request containing pagination and filter parameters
     * @param model the Spring MVC model for passing data to the view fragment
     * @return the logical view name "fragments/dataset :: datatable" for the data table fragment
     * @since 1.0
     */
    @HxRequest
    @GetMapping("/datatable")
    public String datatable(@ModelAttribute DatasetSearchRequest request, Model model) {
        log.debug("Processing HTMX datatable request with limit={}, offset={}, instrument={}, status={}, provider={}",
                request.getLimit(), request.getOffset(), request.getInstrument(), request.getStatus(), request.getProvider());

        var res = hasFilterParameters(request.getInstrument(), request.getStatus(), request.getProvider())
                ? datasetService.searchDatasets(request)
                : datasetService.findDatasets(new PageableRequest(request.getLimit(), request.getOffset()));

        log.info("Retrieved {} datasets out of {} total", res.getContent().size(), res.getTotal());

        PaginationViewHelper.addPaginationAttributes(model, res, "datasets");
        model.addAttribute("searchRequest", request);
        System.out.println(request);

        return "fragments/dataset :: datatable";
    }

    private boolean hasFilterParameters(String instrumentFilter, String status, String providerFilter) {
        return StringUtils.hasValue(instrumentFilter) ||
               StringUtils.hasValue(status) ||
               StringUtils.hasValue(providerFilter);
    }

    /**
     * Handles HTMX requests for instrument selection based on provider.
     * 
     * <p>This endpoint fetches available instruments from the CTrader API
     * for the selected provider and returns them in the instrument selection fragment.</p>
     * 
     * @param providerId the ID of the selected provider
     * @param model the Spring MVC model for passing data to the view fragment
     * @return the logical view name "fragments/download :: instrument-selection"
     * @since 1.0
     */
    @HxRequest
    @GetMapping("/instrument-selection")
    public String getInstrumentSelection(@RequestParam String providerId, Model model) {
        log.debug("Processing HTMX instrument selection request for provider: {}", providerId);
        
        try {
            // For now, only support Pepperstone (provider ID 4)
            if (!"4".equals(providerId)) {
                log.warn("Unsupported provider ID: {}. Only Pepperstone (ID: 4) is supported", providerId);
                model.addAttribute("instruments", Collections.<InstrumentOverview>emptyList());
                return "fragments/download :: instrument-selection";
            }
            
            // Find the enabled CTrader provider configuration (Pepperstone)
            var ctraderProviders = applicationProperties.getProviders().getCtrader();
            var provider = ctraderProviders.stream()
                    .filter(ApplicationProperties.CtraderProvider::isEnabled)
                    .findFirst();
            
            if (provider.isEmpty()) {
                log.warn("No enabled CTrader provider found for Pepperstone");
                model.addAttribute("instruments", Collections.<InstrumentOverview>emptyList());
                return "fragments/download :: instrument-selection";
            }
            
            var ctraderProvider = provider.get();
            log.info("Fetching instruments for CTrader provider: {} (account: {})", 
                    ctraderProvider.getName(), ctraderProvider.getAccountId());
            
            // Fetch available symbols from CTrader API using the provider's account ID
            var symbolsResponse = ctraderFacade.getAvailableSymbols(ctraderProvider.getAccountId(), false)
                    .get();
            
            // Map protobuf symbols to InstrumentOverview objects
            List<InstrumentOverview> instruments = symbolsResponse.getSymbolList()
                    .stream()
                    .map(CtraderMapper::mapToSymbolRecord)
                    .toList();
            
            log.info("Successfully fetched {} instruments for provider: {}", 
                    instruments.size(), ctraderProvider.getName());
            
            model.addAttribute("instruments", instruments);
            
        } catch (Exception e) {
            log.error("Failed to fetch instruments for provider: {}", providerId, e);
            // Return empty list on error to prevent UI breakage
            model.addAttribute("instruments", Collections.<InstrumentOverview>emptyList());
        }
        
        return "fragments/download :: instrument-selection";
    }
}