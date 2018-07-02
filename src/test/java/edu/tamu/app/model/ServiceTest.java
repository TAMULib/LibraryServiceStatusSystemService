package edu.tamu.app.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import edu.tamu.app.StatusApplication;
import edu.tamu.app.enums.Status;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.weaver.auth.model.Credentials;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { StatusApplication.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
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
        assertEquals("The number of Services did not increase by one", initalCount + 1, serviceRepo.count());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testNameNotNull() {
        serviceRepo.create(new Service(null, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testStatusNotNull() {
        serviceRepo.create(new Service(TEST_SERVICE_NAME, null, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
    }

    @Test
    public void testUpdateName() {
        Service service = serviceRepo.create(new Service(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        service.setName(TEST_ALTERNATIVE_SERVICE_NAME);
        service = refreshService(service);
        assertEquals("Service name was not changed", TEST_ALTERNATIVE_SERVICE_NAME, service.getName());
    }

    private Service refreshService(Service service) {
        serviceRepo.save(service);
        return serviceRepo.findOne(service.getId());
    }

    @Test
    public void testUpdateAliases() {
        Service service = serviceRepo.create(new Service(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        service.setAliases(TEST_SERVICE_ALIASES);
        service = refreshService(service);
        assertEquals("Service aliases not set", true, service.getAliases().contains("Alias 1"));
        service.setAliases(TEST_ALTERNATIVE_SERVICE_ALIASES);
        service = refreshService(service);
        assertEquals("Service aliases not updated", true, service.getAliases().contains("Alias 5"));
    }

    @Test
    public void testUpdateStatus() {
        Service service = serviceRepo.create(new Service(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        service.setStatus(TEST_ALTERNATIVE_SERVICE_STATUS);
        service = refreshService(service);
        assertEquals("Service status was not changed", TEST_ALTERNATIVE_SERVICE_STATUS, service.getStatus());
    }

    @Test
    public void testUpdateServiceUrl() {
        Service service = serviceRepo.create(new Service(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        service.setServiceUrl(TEST_SERVICE_URL);
        service = refreshService(service);
        assertEquals("Service status url was not changed", TEST_SERVICE_URL, service.getServiceUrl());
    }

    @Test
    public void testDelete() {
        long initialCount = serviceRepo.count();
        Service service = serviceRepo.create(new Service(TEST_SERVICE_NAME, TEST_ALTERNATIVE_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        assertEquals("The service was not created", initialCount + 1, serviceRepo.count());
        serviceRepo.delete(service);
        assertEquals("The service was not deleted", initialCount, serviceRepo.count());
    }

    @Test
    public void testAssociateProject() {
        Service newService = serviceRepo.create(new Service(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST, TEST_SERVICE_URL, TEST_DESCRIPTION));
        Long serviceId = newService.getId();
        Long projectId = 1L;
        newService.setProjectId(projectId);
        serviceRepo.save(newService);
        Service serviceWithProjectId = serviceRepo.findOne(serviceId);
        assertEquals("The service had the incorrect project id!", projectId, serviceWithProjectId.getProjectId());
    }

    @After
    public void cleanUp() {
        serviceRepo.deleteAll();
        noteRepo.deleteAll();
        userRepo.deleteAll();
    }
}
