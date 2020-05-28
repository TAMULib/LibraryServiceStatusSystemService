package edu.tamu.app.mock.products;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.app.model.request.FeatureRequest;
import edu.tamu.app.model.response.Product;

@Service
@Profile("test")
public class MockProducts {

    private static List<Product> products = new ArrayList<Product>();

    @Value("classpath:mock/products.json")
    private Resource resource;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    private void loadProducts() throws JsonParseException, JsonMappingException, IOException {
        products = objectMapper.readValue(resource.getFile(), new TypeReference<List<Product>>() {});
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public Product getProductById(Long id) {
        Product product = null;
        for (Product currentProduct : products) {
            Optional<Long> currentId = Optional.ofNullable(Long.valueOf(currentProduct.getId()));
            if (currentId.isPresent()) {
                if (currentId.get().equals(id)) {
                    product = currentProduct;
                    break;
                }
            }
        }
        return product;
    }

    public String submitRequest(FeatureRequest request) {
        return "Successfully submitted " + request.getType().getName() + " request!";
    }

}
