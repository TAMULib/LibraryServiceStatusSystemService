package edu.tamu.app.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import edu.tamu.app.WebServerInit;
import edu.tamu.app.enums.Status;
import edu.tamu.app.model.repo.AppUserRepo;
import edu.tamu.app.model.repo.NoteRepo;
import edu.tamu.app.model.repo.ServiceRepo;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { WebServerInit.class })
public class ServiceTest {
    
    protected static final String TEST_SERVICE_NAME = "Test Service Name";
    protected static final String TEST_SERVICE_URL = "https://library.tamu.edu";
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
    protected AppUser testUser;

    @Autowired
    ServiceRepo serviceRepo;
    
    @Autowired
    NoteRepo noteRepo;
    
    @Autowired
    AppUserRepo appUserRepo;
    
    @Before
    public void setUp() {
        testUser = appUserRepo.create("123456789");
    }
    
    @Test
    public void testCreate() {
        long initalCount = serviceRepo.count();
        serviceRepo.create(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST,null);
        assertEquals("The number of Services did not increase by one", initalCount + 1, serviceRepo.count());
    }
    
    @Test(expected = DataIntegrityViolationException.class)
    public void testNameNotNull() {
        serviceRepo.create(null, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST,null);
    }
    
    @Test(expected = DataIntegrityViolationException.class)
    public void testStatusNotNull() {
        serviceRepo.create(TEST_SERVICE_NAME, null, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST,null);
    }
    
    @Test(expected = ConstraintViolationException.class)
    public void testNameNotEmpty() {
        serviceRepo.create("", TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST,null);
    }
    
    @Test
    public void testUpdateName() {
        Service service = serviceRepo.create(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST,null);
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
        Service service = serviceRepo.create(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST,null);
        service.setAliases(TEST_SERVICE_ALIASES);
        service = refreshService(service);
        assertEquals("Service aliases not set", true, service.getAliases().contains("Alias 1"));
        service.setAliases(TEST_ALTERNATIVE_SERVICE_ALIASES);
        service = refreshService(service);
        assertEquals("Service aliases not updated", true, service.getAliases().contains("Alias 5"));
    }
    
    @Test
    public void testUpdateStatus() {
        Service service = serviceRepo.create(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST,null);
        service.setStatus(TEST_ALTERNATIVE_SERVICE_STATUS);
        service = refreshService(service);
        assertEquals("Service status was not changed", TEST_ALTERNATIVE_SERVICE_STATUS, service.getStatus());
    }
    
    @Test
    public void testUpdateServiceUrl() {
        Service service = serviceRepo.create(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST,null);
        service.setServiceUrl(TEST_SERVICE_URL);
        service = refreshService(service);
        assertEquals("Service status url was not changed", TEST_SERVICE_URL, service.getServiceUrl());
    }
    
    @Test
    public void testUpdateNotes() {
        Note note1 = noteRepo.create(TEST_NOTE_TITLE1, testUser);
        Note note2 = noteRepo.create(TEST_NOTE_TITLE2, testUser);
        List<Note> noteList1 = Arrays.asList(note1);
        List<Note> noteList2 = Arrays.asList(note1, note2);
        Service service = serviceRepo.create(TEST_SERVICE_NAME, TEST_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST,null);
        service.setNotes(noteList1);
        service = refreshService(service);
        assertEquals("Service notes did not contain the right number of notes", 1, service.getNotes().size());
        assertEquals("Service notes not set", true, service.getNotes().contains(note1));
        service.setNotes(noteList2);
        service = refreshService(service);
        assertEquals("Service notes did not contain the right number of notes", 2, service.getNotes().size());
        assertEquals("Service notes not updated", true, service.getNotes().contains(note2));
    }
    
    @Test
    public void testDelete() {
        long initialCount = serviceRepo.count();
        Service service = serviceRepo.create(TEST_SERVICE_NAME, TEST_ALTERNATIVE_SERVICE_STATUS, TEST_IS_AUTO, TEST_IS_PUBLIC, TEST_ON_SHORT_LIST,null);
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
