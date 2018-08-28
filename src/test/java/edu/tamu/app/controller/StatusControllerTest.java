package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import edu.tamu.app.enums.OverallMessageType;
import edu.tamu.app.model.OverallStatus;
import edu.tamu.app.service.MonitorService;
import edu.tamu.weaver.response.ApiResponse;

@RunWith(SpringRunner.class)
public class StatusControllerTest {

    private static final String TEST_OVERALL_STATUS_MESSAGE = "Test message";
    private static final OverallMessageType TEST_STATUS = OverallMessageType.SUCCESS;

    private static OverallStatus TEST_OVERALL_STATUS = new OverallStatus(TEST_STATUS, TEST_OVERALL_STATUS_MESSAGE);

    private ApiResponse response;

    @Mock
    private MonitorService monitorService;

    @InjectMocks
    private StatusController statusController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(monitorService.getOverallStatus()).thenReturn(TEST_OVERALL_STATUS);
        when(monitorService.getOverallStatusPublic()).thenReturn(TEST_OVERALL_STATUS);
    }

    @Test
    public void testOverallFull() {
        response = statusController.overallFull();
        assertEquals("Response was not successfull", SUCCESS, response.getMeta().getStatus());
    }

    @Test
    public void testOverallPublic() {
        response = statusController.overallPublic();
        assertEquals("Response was not successfull", SUCCESS, response.getMeta().getStatus());
    }
}
