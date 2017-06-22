/* 
 * ThemeController.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.app.controller;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.JsonNode;

import edu.tamu.framework.aspect.annotation.ApiData;
import edu.tamu.framework.aspect.annotation.ApiMapping;
import edu.tamu.framework.aspect.annotation.ApiModel;
import edu.tamu.framework.aspect.annotation.Auth;
import edu.tamu.framework.model.ApiResponse;
import edu.tamu.framework.model.CoreTheme;
import edu.tamu.framework.model.repo.CoreThemeRepo;
import edu.tamu.framework.service.ThemeManagerService;

/**
 * 
 */
@Controller
@ApiMapping("/theme")
public class ThemeController {

    @Autowired
    private CoreThemeRepo coreThemeRepo;

    @Autowired
    private ThemeManagerService themeManagerService;
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 
     * @return
     */
    @ApiMapping("/all")
    @Auth(role = "ROLE_ADMIN")
    public ApiResponse getAll() {
        return new ApiResponse(SUCCESS, coreThemeRepo.findAll());
    }

    /**
     * 
     * @param data
     * @return
     * @throws IOException
     */
    @ApiMapping("/update-property")
    @Auth(role = "ROLE_ADMIN")
    public ApiResponse updateProperty(@ApiData JsonNode dataNode) throws IOException {
        Long themeId = dataNode.get("themeId").asLong();
        Long propertyId = dataNode.get("propertyId").asLong();
        String value = dataNode.get("value").asText();
        themeManagerService.updateThemeProperty(themeId, propertyId, value);
        simpMessagingTemplate.convertAndSend("/channel/theme", new ApiResponse(SUCCESS, coreThemeRepo.findAll()));
        return new ApiResponse(SUCCESS, "Theme updated");
    }

    /**
     * 
     * @param data
     * @return
     * @throws IOException
     */
    @ApiMapping("/add-theme")
    @Auth(role = "ROLE_ADMIN")
    public ApiResponse addTheme(@ApiModel CoreTheme newTheme) throws IOException {
        coreThemeRepo.create(newTheme.getName());
        simpMessagingTemplate.convertAndSend("/channel/theme", new ApiResponse(SUCCESS, coreThemeRepo.findAll()));
        return new ApiResponse(SUCCESS, "Theme added");
    }
    
    /**
     * 
     * @param data
     * @return
     * @throws IOException
     */
    @ApiMapping("/remove-theme")
    @Auth(role = "ROLE_ADMIN")
    public ApiResponse removeTheme(@ApiModel CoreTheme themeToRemove) throws IOException {
        coreThemeRepo.delete(themeToRemove);
        simpMessagingTemplate.convertAndSend("/channel/theme", new ApiResponse(SUCCESS, coreThemeRepo.findAll()));
        return new ApiResponse(SUCCESS, "Theme removed");
    }

    /**
     * 
     * @param data
     * @return
     * @throws IOException
     */
    @ApiMapping("/activate-theme")
    @Auth(role = "ROLE_ADMIN")
    public ApiResponse activateTheme(@ApiModel CoreTheme themeToActivate) throws IOException {
        themeManagerService.setCurrentTheme(themeToActivate);
        simpMessagingTemplate.convertAndSend("/channel/theme", new ApiResponse(SUCCESS, coreThemeRepo.findAll()));
        return new ApiResponse(SUCCESS, "Theme activated");
    }
}
