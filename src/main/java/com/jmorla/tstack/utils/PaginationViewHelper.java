package com.jmorla.tstack.utils;

import com.jmorla.tstack.models.PagedResponse;
import lombok.experimental.UtilityClass;
import org.springframework.ui.Model;

/**
 * Utility class for adding pagination attributes to Spring MVC view models.
 * 
 * <p>This utility simplifies the process of adding pagination data from a 
 * {@link PagedResponse} to a Spring MVC {@link Model}, eliminating boilerplate 
 * code in controllers and ensuring consistent pagination attribute naming 
 * across the application.</p>
 * 
 * @author Jorge Morla
 * @version 1.0
 * @since 1.0
 */
@UtilityClass
public class PaginationViewHelper {

    /**
     * Adds pagination attributes to the model using default content attribute name.
     * 
     * <p>This method adds the following attributes to the model:</p>
     * <ul>
     *   <li><strong>content</strong> - the paginated data collection</li>
     *   <li><strong>limit</strong> - the page size</li>
     *   <li><strong>offset</strong> - the starting position</li>
     *   <li><strong>total</strong> - the total number of records</li>
     * </ul>
     * 
     * @param model the Spring MVC model to add attributes to
     * @param pagedResponse the paged response containing pagination data and content
     * @param <T> the type of content in the paged response
     * @throws IllegalArgumentException if model or pagedResponse is null
     * @since 1.0
     */
    public static <T> void addPaginationAttributes(Model model, PagedResponse<T> pagedResponse) {
        addPaginationAttributes(model, pagedResponse, "content");
    }

    /**
     * Adds pagination attributes to the model with a custom content attribute name.
     * 
     * <p>This method adds the following attributes to the model:</p>
     * <ul>
     *   <li><strong>{contentAttributeName}</strong> - the paginated data collection</li>
     *   <li><strong>limit</strong> - the page size</li>
     *   <li><strong>offset</strong> - the starting position</li>
     *   <li><strong>total</strong> - the total number of records</li>
     * </ul>
     * 
     * @param model the Spring MVC model to add attributes to
     * @param pagedResponse the paged response containing pagination data and content
     * @param contentAttributeName the custom name for the content attribute
     * @param <T> the type of content in the paged response
     * @throws IllegalArgumentException if model, pagedResponse, or contentAttributeName is null
     * @since 1.0
     */
    public static <T> void addPaginationAttributes(Model model, PagedResponse<T> pagedResponse, String contentAttributeName) {
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null");
        }
        if (pagedResponse == null) {
            throw new IllegalArgumentException("PagedResponse cannot be null");
        }
        if (contentAttributeName == null) {
            throw new IllegalArgumentException("Content attribute name cannot be null");
        }

        model.addAttribute(contentAttributeName, pagedResponse.getContent());
        model.addAttribute("limit", pagedResponse.getLimit());
        model.addAttribute("offset", pagedResponse.getOffset());
        model.addAttribute("total", pagedResponse.getTotal());
    }
}