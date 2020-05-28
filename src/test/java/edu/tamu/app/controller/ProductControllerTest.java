package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

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

@RunWith(SpringRunner.class)
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

    @Before
    public void setup() throws JsonParseException, JsonMappingException, IOException {
        MockitoAnnotations.initMocks(this);
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
        assertEquals("Response was not a success!", ApiStatus.SUCCESS, response.getMeta().getStatus());
        List<Product> products = (List<Product>) response.getPayload().get("ArrayList<Product>");
        assertEquals("Products response size was not as expected!", products.size(), products.size());
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            assertEquals(i + " Product did not have the correct id!", products.get(i).getId(), product.getId());
            assertEquals(i + " Product did not have the correct name!", products.get(i).getName(), product.getName());
        }
    }

    @Test
    public void getById() throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
        ApiResponse response = productController.getById(1L);
        assertEquals("Response was not a success!", ApiStatus.SUCCESS, response.getMeta().getStatus());
        Product product = (Product) response.getPayload().get("Product");
        assertNotNull("Product is null!", product);
        assertEquals("Product did not have the correct id!", products.get(0).getId(), product.getId());
        assertEquals("Product did not have the correct name!", products.get(0).getName(), product.getName());
    }

    @Test
    public void testSubmitFeatureRequest() throws UserNotFoundException {
        ApiResponse response = productController.submitFeatureRequest(TEST_FEATURE_PROPOSAL);
        assertEquals("Request was not successfull", SUCCESS, response.getMeta().getStatus());
    }

}
