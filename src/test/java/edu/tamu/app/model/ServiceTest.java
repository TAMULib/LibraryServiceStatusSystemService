package edu.tamu.app.model;

import static org.junit.Assert.assertEquals;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import edu.tamu.app.WebServerInit;
import edu.tamu.app.enums.Status;
import edu.tamu.app.model.repo.ServiceRepo;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { WebServerInit.class })
public class ServiceTest {
    
    protected static final String TEST_SERVICE_NAME = "Test Service Name";
    protected static final Status TEST_SERVICE_STATUS = Status.UP;

    @Autowired
    ServiceRepo serviceRepo;
    
    @Test
    public void testCreate() {
        long initalCount = serviceRepo.count();
        serviceRepo.create(TEST_SERVICE_NAME, TEST_SERVICE_STATUS);
        assertEquals("The number of Services did not increase by one", initalCount + 1, serviceRepo.count());
    }
    
    @Test(expected = DataIntegrityViolationException.class)
    public void testNameNotNull() {
        serviceRepo.create(null, TEST_SERVICE_STATUS);
    }
    
    @Test(expected = DataIntegrityViolationException.class)
    public void testStatusNotNull() {
        serviceRepo.create(TEST_SERVICE_NAME, null);
    }
    
    @Test(expected = ConstraintViolationException.class)
    public void testNameNotEmpty() {
        serviceRepo.create("", TEST_SERVICE_STATUS);
    }
    
    @Test
    public void testUpdate() {
        TODO
    }
}
