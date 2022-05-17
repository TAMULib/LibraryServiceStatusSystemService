package edu.tamu.app.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.DataIntegrityViolationException;
import edu.tamu.app.StatusApplication;
import edu.tamu.app.enums.Status;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.weaver.auth.model.Credentials;

@SpringBootTest(classes = { StatusApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ServiceTest {

    private static final String TEST_SERVICE_NAME = "Test Service Name";
    private static final String TEST_SERVICE_URL = "https://library.tamu.edu";
    private static final String TEST_DESCRIPTION = "Test Service Description";
    private static final Status TEST_SERVICE_STATUS = Status.UP;
    private static final List<String> TEST_SERVICE_ALIASES = Arrays.asList("Alias 1", "Alias 2", "Alias 3");
    private static final String TEST_ALTERNATIVE_SERVICE_NAME = "Different Service Name";
    private static final Boolean TEST_IS_AUTO = false;
    private static final Boolean TEST_IS_PUBLIC = true;
    private static final Boolean TEST_ON_SHORT_LIST = true;
    private static final Status TEST_ALTERNATIVE_SERVICE_STATUS = Status.DOWN;
    private static final List<String> TEST_ALTERNATIVE_SERVICE_ALIASES = Arrays.asList("Alias 4", "Alias 5", "Alias 6");

    private static final Credentials TEST_CREDENTIALS = new Credentials();
    {
        TEST_CREDENTIALS.setUin("123456789");
        TEST_CREDENTIALS.setEmail("aggieJack@tamu.edu");
        TEST_CREDENTIALS.setFirstName("Aggie");
        TEST_CREDENTIALS.setLastName("Jack");
        TEST_CREDENTIALS.setRole("ROLE_USER");
    }

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private NoteRepo noteRepo;

    @Autowired
    private UserRepo userRepo;

    @Test
    public void testCreate() {
        long initalCount = serviceRepo.count();
        serviceRepo.create(new Service(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        assertEquals(initalCount + 1, serviceRepo.count(), "The number of Services did not increase by one");
    }

    @Test
    public void testNameNotNull() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            serviceRepo.create(new Service(null, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        });
    }

    @Test
    public void testStatusNotNull() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            serviceRepo.create(new Service(TEST_SERVICE_NAME, null, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        });
    }

    @Test
    public void testUpdateName() {
        Service service = serviceRepo.create(new Service(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        service.setName(TEST_ALTERNATIVE_SERVICE_NAME);
        service = refreshService(service);
        assertEquals(TEST_ALTERNATIVE_SERVICE_NAME, service.getName(), "Service name was not changed");
    }

    private Service refreshService(Service service) {
        serviceRepo.save(service);
        return serviceRepo.findById(service.getId()).get();
    }

    @Test
    public void testUpdateAliases() {
        Service service = serviceRepo.create(new Service(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        service.setAliases(TEST_SERVICE_ALIASES);
        service = refreshService(service);
        assertEquals(true, service.getAliases().contains("Alias 1"), "Service aliases not set");
        service.setAliases(TEST_ALTERNATIVE_SERVICE_ALIASES);
        service = refreshService(service);
        assertEquals(true, service.getAliases().contains("Alias 5"), "Service aliases not updated");
    }

    @Test
    public void testUpdateStatus() {
        Service service = serviceRepo.create(new Service(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        service.setStatus(TEST_ALTERNATIVE_SERVICE_STATUS);
        service = refreshService(service);
        assertEquals(TEST_ALTERNATIVE_SERVICE_STATUS, service.getStatus(), "Service status was not changed");
    }

    @Test
    public void testUpdateServiceUrl() {
        Service service = serviceRepo.create(new Service(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        service.setServiceUrl(TEST_SERVICE_URL);
        service = refreshService(service);
        assertEquals(TEST_SERVICE_URL, service.getServiceUrl(), "Service status url was not changed");
    }

    @Test
    public void testDelete() {
        long initialCount = serviceRepo.count();
        Service service = serviceRepo.create(new Service(TEST_SERVICE_NAME, TEST_ALTERNATIVE_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        assertEquals(initialCount + 1, serviceRepo.count(), "The service was not created");
        serviceRepo.delete(service);
        assertEquals(initialCount, serviceRepo.count(), "The service was not deleted");
    }

    @Test
    public void testAssociateProduct() {
        Service newService = serviceRepo.create(new Service(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        Long serviceId = newService.getId();
        Long productId = 1L;
        newService.setProductId(productId);
        serviceRepo.save(newService);
        Service serviceWithProductId = serviceRepo.findById(serviceId).get();
        assertEquals(productId, serviceWithProductId.getProductId(), "The service had the incorrect product id!");
    }

    @AfterEach
    public void cleanUp() {
        serviceRepo.deleteAll();
        noteRepo.deleteAll();
        userRepo.deleteAll();
    }
}
