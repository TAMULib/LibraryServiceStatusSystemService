package edu.tamu.app.mock.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.app.mock.products.MockProducts;
import edu.tamu.app.model.request.FeatureRequest;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@Profile("test")
@RequestMapping("/mock/products")
public class MockProductManagementController {

    @Autowired
    private MockProducts mockProducts;

    @RequestMapping
    public ApiResponse getAll() {
        return new ApiResponse(SUCCESS, mockProducts.getAllProducts());
    }

    @RequestMapping("/{id}")
    public ApiResponse getById(@PathVariable Long id) {
        return new ApiResponse(SUCCESS, mockProducts.getProductById(id));
    }

    @RequestMapping("/issue")
    public ApiResponse submitIssue(@RequestBody FeatureRequest request) {
        return new ApiResponse(SUCCESS, mockProducts.submitRequest(request));
    }

    @RequestMapping("/feature")
    public ApiResponse submitFeature(@RequestBody FeatureRequest request) {
        return new ApiResponse(SUCCESS, mockProducts.submitRequest(request));
    }

}
