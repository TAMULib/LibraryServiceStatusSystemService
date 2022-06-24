package edu.tamu.app.model.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.tamu.app.model.request.AbstractRequest.RequestType;

@ExtendWith(SpringExtension.class)
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
        assertEquals(RequestType.FEATURE, serviceRequest.getType(), "Type was not set");
        assertEquals(TEST_TITLE, serviceRequest.getTitle(), "Title was not set");
        assertEquals(TEST_DESCRIPTION, serviceRequest.getDescription(), "Description was not set");
        assertEquals(TEST_SERVICE_ID, serviceRequest.getService(), "Service ID was not set");

        // Constructor with email
        ServiceRequest serviceRequestWithEmail = new ServiceRequest(TEST_REQUEST_TYPE, TEST_TITLE, TEST_DESCRIPTION, TEST_SERVICE_ID, TEST_EMAIL);
        assertEquals(RequestType.FEATURE, serviceRequestWithEmail.getType(), "Type was not set");
        assertEquals(TEST_TITLE, serviceRequestWithEmail.getTitle(), "Title was not set");
        assertEquals(TEST_DESCRIPTION, serviceRequestWithEmail.getDescription(), "Description was not set");
        assertEquals(TEST_SERVICE_ID, serviceRequestWithEmail.getService(), "Service ID was not set");
        assertEquals(TEST_EMAIL, serviceRequestWithEmail.getEmail(), "Email was not set");
    }
}
