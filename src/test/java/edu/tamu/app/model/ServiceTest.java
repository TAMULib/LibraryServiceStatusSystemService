package edu.tamu.app.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import edu.tamu.app.WebServerInit;
import edu.tamu.app.enums.Role;
import edu.tamu.app.enums.Status;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.weaver.auth.model.Credentials;

@WebAppConfiguration
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { WebServerInit.class })
public class ServiceTest {

    protected static final String TEST_SERVICE_NAME = "Test Service Name";
    protected static final String TEST_SERVICE_URL = "https://library.tamu.edu";
    protected static final String TEST_DESCRIPTION = "Test Service Description";
    protected static final Status TEST_SERVICE_STATUS = Status.UP;
    protected static final List<String> TEST_SERVICE_ALIASES = Arrays.asList("Alias 1", "Alias 2", "Alias 3");
    protected static final String TEST_NOTE_TITLE1 = "Note 1";
    protected static final String TEST_NOTE_TITLE2 = "Note 2";
    protected static final String TEST_ALTERNATIVE_SERVICE_NAME = "Different Service Name";
    protected static final Boolean TEST_IS_AUTO = false;
    protected static final Boolean TEST_IS_PUBLIC = true;
    protected static final Boolean TEST_ON_SHORT_LIST = true;
    protected static final Status TEST_ALTERNATIVE_SERVICE_STATUS = Status.DOWN;
    protected static final List<String> TEST_ALTERNATIVE_SERVICE_ALIASES = Arrays.asList("Alias 4", "Alias 5", "Alias 6");
    protected User testUser;

    protected static final Credentials TEST_CREDENTIALS = new Credentials();
    {
        TEST_CREDENTIALS.setUin("123456789");
        TEST_CREDENTIALS.setEmail("aggieJack@tamu.edu");
        TEST_CREDENTIALS.setFirstName("Aggie");
        TEST_CREDENTIALS.setLastName("Jack");
        TEST_CREDENTIALS.setRole("ROLE_USER");
    }

    @Autowired
    ServiceRepo serviceRepo;

    @Autowired
    NoteRepo noteRepo;

    @Autowired
    UserRepo appUserRepo;

    @Before
    public void setUp() {
        testUser = appUserRepo.create(TEST_CREDENTIALS.getUin(), TEST_CREDENTIALS.getEmail(), TEST_CREDENTIALS.getFirstName(), TEST_CREDENTIALS.getLastName(), Role.valueOf(TEST_CREDENTIALS.getRole()));
    }

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

    @After
    public void cleanUp() {
        serviceRepo.deleteAll();
        noteRepo.deleteAll();
        appUserRepo.deleteAll();
    }
}
