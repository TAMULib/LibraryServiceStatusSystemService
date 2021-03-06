package edu.tamu.app.controller;

import static edu.tamu.weaver.response.ApiStatus.ERROR;
import static edu.tamu.weaver.response.ApiStatus.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import edu.tamu.app.enums.Role;
import edu.tamu.app.model.User;
import edu.tamu.app.model.repo.UserRepo;
import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.response.ApiResponse;

@RunWith(SpringRunner.class)
public class UserControllerTest {

    private static final Credentials TEST_CREDENTIALS_1 = new Credentials();
    static {
        TEST_CREDENTIALS_1.setUin("123456789");
        TEST_CREDENTIALS_1.setEmail("aggieJack@tamu.edu");
        TEST_CREDENTIALS_1.setFirstName("Aggie");
        TEST_CREDENTIALS_1.setLastName("Jack");
        TEST_CREDENTIALS_1.setRole("ROLE_USER");
    }

    private static final Credentials TEST_CREDENTIALS_2 = new Credentials();
    static {
        TEST_CREDENTIALS_2.setUin("987654321");
        TEST_CREDENTIALS_2.setEmail("aggieJack@tamu.edu");
        TEST_CREDENTIALS_2.setFirstName("Aggie");
        TEST_CREDENTIALS_2.setLastName("Jack");
        TEST_CREDENTIALS_2.setRole("ROLE_USER");
    }

    private User testUser1 = new User(TEST_CREDENTIALS_1.getUin(), TEST_CREDENTIALS_1.getEmail(), TEST_CREDENTIALS_1.getFirstName(), TEST_CREDENTIALS_1.getLastName(), Role.valueOf(TEST_CREDENTIALS_1.getRole()));
    private User testUser2 = new User(TEST_CREDENTIALS_2.getUin(), TEST_CREDENTIALS_2.getEmail(), TEST_CREDENTIALS_2.getFirstName(), TEST_CREDENTIALS_2.getLastName(), Role.valueOf(TEST_CREDENTIALS_2.getRole()));

    private List<User> mockUserList = new ArrayList<User>(Arrays.asList(new User[] { testUser1, testUser2 }));

    private static ApiResponse apiResponse;

    @Mock
    private UserRepo userRepo;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() {
        when(userRepo.findAll()).thenReturn(mockUserList);
        when(userRepo.save(any(User.class))).thenReturn(testUser1);
        doNothing().when(simpMessagingTemplate).convertAndSend(any(String.class), any(Object.class));
    }

    @Test
    public void testCredentials() {
        apiResponse = userController.credentials(TEST_CREDENTIALS_1);
        assertEquals("Unable to get user credentials", SUCCESS, apiResponse.getMeta().getStatus());
    }

    @Test
    public void testNullCredentials() {
        apiResponse = userController.credentials(null);
        assertEquals("Unable to get user credentials", ERROR, apiResponse.getMeta().getStatus());
    }

    @Test
    public void testGetUser() {
        apiResponse = userController.getUser(testUser1);
        assertEquals("Unable to get user", SUCCESS, apiResponse.getMeta().getStatus());
    }

    @Test
    public void testGetNullUser() {
        apiResponse = userController.getUser(null);
        assertEquals("Unable to get user", ERROR, apiResponse.getMeta().getStatus());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAllUsers() throws Exception {
        apiResponse = userController.allUsers();
        assertEquals("Request for users was unsuccessful", SUCCESS, apiResponse.getMeta().getStatus());
        assertEquals("Number of users was not correct", 2, ((ArrayList<User>) apiResponse.getPayload().get("ArrayList<User>")).size());
    }

    @Test
    public void testUpdateUser() throws Exception {
        apiResponse = userController.updateUser(testUser1);
        assertEquals("User was not successfully updated", SUCCESS, apiResponse.getMeta().getStatus());
    }
}
