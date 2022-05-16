package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.model.FeatureProposal;
import edu.tamu.app.model.response.Product;
import edu.tamu.app.service.ProductService;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.response.ApiStatus;

@ExtendWith(SpringExtension.class)
public class ProductControllerTest {

    private static String TEST_FEATURE_PROPOSAL_NAME = "Test FP name";
    private static String TEST_FEATURE_PROPOSAL_DESCRIPTION = "Test FP name";
    private static FeatureProposal TEST_FEATURE_PROPOSAL = new FeatureProposal(TEST_FEATURE_PROPOSAL_NAME, TEST_FEATURE_PROPOSAL_DESCRIPTION);
    private static List<Product> products = new ArrayList<Product>();

    @Value("classpath:mock/products.json")
    private Resource resource;

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setup() throws JsonParseException, JsonMappingException, IOException {
        MockitoAnnotations.openMocks(this);
        products = objectMapper.readValue(resource.getFile(), new TypeReference<List<Product>>() {
        });
        when(productService.getAll()).thenReturn(new ApiResponse(SUCCESS, products));
        when(productService.getById(any(Long.class))).thenReturn(new ApiResponse(SUCCESS, products.get(0)));
        when(productService.submitFeatureRequest(any(FeatureProposal.class))).thenReturn(new ApiResponse(SUCCESS, TEST_FEATURE_PROPOSAL));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getAll() throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
        ApiResponse response = productController.getAll();
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus(), "Response was not a success!");
        List<Product> products = (List<Product>) response.getPayload().get("ArrayList<Product>");
        assertEquals(products.size(), products.size(), "Products response size was not as expected!");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            assertEquals(products.get(i).getId(), product.getId(), i + " Product did not have the correct id!");
            assertEquals(products.get(i).getName(), product.getName(), i + " Product did not have the correct name!");
        }
    }

    @Test
    public void getById() throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
        ApiResponse response = productController.getById(1L);
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus(), "Response was not a success!");
        Product product = (Product) response.getPayload().get("Product");
        assertNotNull(product, "Product is null!");
        assertEquals(products.get(0).getId(), product.getId(), "Product did not have the correct id!");
        assertEquals(products.get(0).getName(), product.getName(), "Product did not have the correct name!");
    }

    @Test
    public void testSubmitFeatureRequest() throws UserNotFoundException {
        ApiResponse response = productController.submitFeatureRequest(TEST_FEATURE_PROPOSAL);
        assertEquals(SUCCESS, response.getMeta().getStatus(), "Request was not successfull");
    }

}
