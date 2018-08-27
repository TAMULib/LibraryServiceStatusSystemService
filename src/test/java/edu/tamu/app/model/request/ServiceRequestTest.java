package edu.tamu.app.model.request;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import edu.tamu.app.model.request.AbstractRequest.RequestType;

@RunWith(SpringRunner.class)
public class ServiceRequestTest {

    private static final RequestType TEST_REQUEST_TYPE = RequestType.FEATURE;
    private static final String TEST_TITLE = "Test Request title";
    private static final String TEST_DESCRIPTION = "Test Request Description";
    private static final Long TEST_SERVICE_ID = 1L;
    private static final String TEST_EMAIL = "aggiejack@mailinator.com";

    @Test
    public void testConstructors() {
        // Constructor without email
        ServiceRequest serviceRequest = new ServiceRequest(TEST_REQUEST_TYPE, TEST_TITLE, TEST_DESCRIPTION, TEST_SERVICE_ID);
        assertEquals("Type was not set", RequestType.FEATURE, serviceRequest.getType());
        assertEquals("Title was not set", TEST_TITLE, serviceRequest.getTitle());
        assertEquals("Description was not set", TEST_DESCRIPTION, serviceRequest.getDescription());
        assertEquals("Service ID was not set", TEST_SERVICE_ID, serviceRequest.getService());

        // Constructor with email
        ServiceRequest serviceRequestWithEmail = new ServiceRequest(TEST_REQUEST_TYPE, TEST_TITLE, TEST_DESCRIPTION, TEST_SERVICE_ID, TEST_EMAIL);
        assertEquals("Type was not set", RequestType.FEATURE, serviceRequestWithEmail.getType());
        assertEquals("Title was not set", TEST_TITLE, serviceRequestWithEmail.getTitle());
        assertEquals("Description was not set", TEST_DESCRIPTION, serviceRequestWithEmail.getDescription());
        assertEquals("Service ID was not set", TEST_SERVICE_ID, serviceRequestWithEmail.getService());
        assertEquals("Email was not set", TEST_EMAIL, serviceRequestWithEmail.getEmail());
    }
}
