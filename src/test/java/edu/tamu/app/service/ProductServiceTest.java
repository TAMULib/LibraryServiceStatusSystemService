package edu.tamu.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.app.StatusApplication;
import edu.tamu.app.enums.Role;
import edu.tamu.app.enums.Status;
import edu.tamu.app.exception.UserNotFoundException;
import edu.tamu.app.mock.products.MockProducts;
import edu.tamu.app.model.FeatureProposal;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.User;
import edu.tamu.app.model.repo.FeatureProposalRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.app.model.request.AbstractRequest;
import edu.tamu.app.model.request.AbstractRequest.RequestType;
import edu.tamu.app.model.request.IssueRequest;
import edu.tamu.app.model.response.Product;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.response.ApiStatus;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { StatusApplication.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
public class ProductServiceTest {

    private static final String TEST_SERVICE_NAME = "Test Service Name";
    private static final String TEST_SERVICE_URL = "https://library.tamu.edu";
    private static final String TEST_SERVICE_DESCRIPTION = "Test Service Description";
    private static final Boolean TEST_IS_AUTO = false;
    private static final Boolean TEST_IS_PUBLIC = true;
    private static final Boolean TEST_ON_SHORT_LIST = true;
    private static final Status TEST_SERVICE_STATUS = Status.UP;

    private static final String TEST_FEATURE_PROPOSAL_TITLE = "Feature Proposal Title";
    private static final String TEST_FEATURE_PROPOSAL_DESCRIPTION = "Test Feature Proposal Description";

    private static final Credentials TEST_CREDENTIALS = new Credentials();
    {
        TEST_CREDENTIALS.setUin("123456789");
        TEST_CREDENTIALS.setEmail("aggieJack@tamu.edu");
        TEST_CREDENTIALS.setFirstName("Aggie");
        TEST_CREDENTIALS.setLastName("Jack");
        TEST_CREDENTIALS.setRole("ROLE_USER");
    }

    @Autowired
    private MockProducts mockReader;

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private FeatureProposalRepo featureProposalRepo;

    @Test
    public void getAll() throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
        ApiResponse response = productService.getAll();
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus(), "Response was not a success!");
        List<Product> products = objectMapper.convertValue(response.getPayload().get("ArrayList<Product>"), new TypeReference<List<Product>>() {
        });
        List<Product> mockProducts = mockReader.getAllProducts();
        assertEquals(mockProducts.size(), products.size(), "Products response size was not as expected!");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            assertEquals(mockProducts.get(i).getId(), product.getId(), i + " Product did not have the correct id!");
            assertEquals(mockProducts.get(i).getName(), product.getName(), i + " Product did not have the correct name!");
        }
    }

    @Test
    public void getById() throws JsonParseException, JsonMappingException, MalformedURLException, IOException {
        Long id = 1L;
        ApiResponse response = productService.getById(id);
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus(), "Response was not a success!");
        Product product = objectMapper.convertValue(response.getPayload().get("Product"), Product.class);
        assertNotNull(product, "Product is null!");
        Product mockProduct = mockReader.getProductById(id);
        assertEquals(mockProduct.getId(), product.getId(), "Product did not have the correct id!");
        assertEquals(mockProduct.getName(), product.getName(), "Product did not have the correct name!");
    }

    @Test
    public void submitFeatureRequest() throws UserNotFoundException {
        User testUser = userRepo.create(TEST_CREDENTIALS.getUin(), TEST_CREDENTIALS.getEmail(), TEST_CREDENTIALS.getFirstName(), TEST_CREDENTIALS.getLastName(), Role.valueOf(TEST_CREDENTIALS.getRole()));
        Service service = serviceRepo.create(new Service(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_SERVICE_DESCRIPTION));
        FeatureProposal newFeatureProposal = featureProposalRepo.create(new FeatureProposal(TEST_FEATURE_PROPOSAL_TITLE, TEST_FEATURE_PROPOSAL_DESCRIPTION, testUser, service), TEST_CREDENTIALS);
        ApiResponse response = productService.submitFeatureRequest(newFeatureProposal);
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus(), "Response was not a success!");
        assertEquals("Successfully submitted " + RequestType.FEATURE.getName() + " request!", response.getMeta().getMessage(), "Response message was not correct!");
    }

    @Test
    public void submitIssueRequest() {
        IssueRequest request = new IssueRequest(AbstractRequest.RequestType.ISSUE, "Test issue request", "This is a test issue request on service Cap!", "Cap", new Credentials());
        ApiResponse response = productService.submitIssueRequest(request);
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus(), "Response was not a success!");
        assertEquals("Successfully submitted " + request.getType().getName() + " request!", response.getMeta().getMessage(), "Response message was not correct!");
    }

    @AfterEach
    public void cleanUp() {
        featureProposalRepo.deleteAll();
        serviceRepo.deleteAll();
        userRepo.deleteAll();
    }

}
