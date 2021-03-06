package edu.tamu.app.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.FeatureProposal;
import edu.tamu.app.service.ProductService;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.validation.aspect.annotation.WeaverValidatedModel;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse getAll() throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
        return productService.getAll();
    }

    @RequestMapping("/{id}")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ApiResponse getById(@PathVariable Long id) throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
        return productService.getById(id);
    }

    @RequestMapping("/feature")
    @PreAuthorize("hasRole('SERVICE_MANAGER')")
    public ApiResponse submitFeatureRequest(@WeaverValidatedModel FeatureProposal proposal) throws UserNotFoundException {
        return productService.submitFeatureRequest(proposal);
    }

}
