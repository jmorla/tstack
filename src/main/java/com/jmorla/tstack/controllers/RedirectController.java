package com.jmorla.tstack.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsible for handling root-level redirects in the application.
 * 
 * <p>This controller manages the routing behavior for the application's entry point,
 * ensuring users are directed to the appropriate landing page when accessing the
 * root URL. It serves as a centralized location for managing top-level navigation
 * redirects.</p>
 * 
 * @author TStack Development Team
 * @version 1.0
 * @since 1.0
 */
@Controller
public class RedirectController {

    /**
     * Redirects the root URL request to the datasets page.
     * 
     * <p>This method handles GET requests to the application root ("/") and
     * automatically redirects users to the datasets listing page. This ensures
     * that users accessing the base URL are immediately taken to the main
     * functionality of the application.</p>
     * 
     * @return a redirect URL string pointing to the datasets page
     * @since 1.0
     */
    @GetMapping("/")
    public String redirectToDownload() {
        return "redirect:/datasets";
    }
}
