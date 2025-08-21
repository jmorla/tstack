package com.jmorla.tstack.controllers;

import com.jmorla.tstack.models.FindDatasetRequest;
import com.jmorla.tstack.services.DatasetService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    /**
     * Provides the active menu identifier for all controller methods.
     * 
     * <p>This method is annotated with {@code @ModelAttribute} to automatically
     * add the active menu state to all views rendered by this controller.</p>
     * 
     * @return the string "datasets" indicating the active menu section
     * @since 1.0
     */
    @ModelAttribute("activeMenu")
    public String activeMenu() {
        return "datasets";
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
     * @param limit the maximum number of records to return (default: 5)
     * @param offset the number of records to skip for pagination (default: 0)
     * @param model the Spring MVC model for passing data to the view fragment
     * @return the logical view name "fragments/dataset :: datatable" for the data table fragment
     * @since 1.0
     */
    @HxRequest
    @GetMapping("/datatable")
    public String datatable(@RequestParam(defaultValue = "5") int limit,
                           @RequestParam(defaultValue = "0") int offset,
                           Model model) {
        log.debug("Processing HTMX datatable request with limit={}, offset={}", limit, offset);

        // REMOVE THIS LINE LATER
        delay(3000);

        var res = datasetService.findDatasets(FindDatasetRequest.builder()
                        .limit(limit)
                        .offset(offset)
                .build());

        log.info("Retrieved {} datasets out of {} total", res.getDatasets().size(), res.getTotal());

        model.addAttribute("datasets", res.getDatasets());
        model.addAttribute("limit", res.getLimit());
        model.addAttribute("offset", res.getOffset());
        model.addAttribute("total", res.getTotal());

        return "fragments/dataset :: datatable";
    }

    private static void delay(int delay) {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}